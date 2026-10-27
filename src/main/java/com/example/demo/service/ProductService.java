package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.factory.SanPhamFactory;
import com.example.demo.repository.ChiTietPhieuNhapRepository;
import com.example.demo.repository.ChiTietPhieuXuatRepository;
import com.example.demo.repository.LoHangRepository;
import com.example.demo.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final LoHangRepository loHangRepository;
    private final ChiTietPhieuNhapRepository chiTietPhieuNhapRepository;
    private final ChiTietPhieuXuatRepository chiTietPhieuXuatRepository;
    private final SanPhamFactory sanPhamFactory;

    public ProductService(ProductRepository productRepository,
                          LoHangRepository loHangRepository,
                          ChiTietPhieuNhapRepository chiTietPhieuNhapRepository,
                          ChiTietPhieuXuatRepository chiTietPhieuXuatRepository,
                          SanPhamFactory sanPhamFactory) {
        this.productRepository = productRepository;
        this.loHangRepository = loHangRepository;
        this.chiTietPhieuNhapRepository = chiTietPhieuNhapRepository;
        this.chiTietPhieuXuatRepository = chiTietPhieuXuatRepository;
        this.sanPhamFactory = sanPhamFactory;
    }

    @Transactional(readOnly = true)
    public List<Product> layTatCaSanPham() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product laySanPhamTheoId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
    }

    @Transactional(readOnly = true)
    public long layTongSoSanPham() {
        return productRepository.countTotalProducts();
    }

    /** Tổng tồn kho, gồm cả lô đã hết hạn. */
    @Transactional(readOnly = true)
    public long layTongTonKho() {
        return productRepository.sumTotalInventory();
    }

    /** Tồn kho khả dụng (không tính lô đã hết hạn). */
    @Transactional(readOnly = true)
    public long layTongTonKhoKhaDung() {
        return productRepository.sumAvailableInventory(LocalDate.now());
    }

    @Transactional(readOnly = true)
    public boolean tonTaiMaSanPham(String ma) {
        return productRepository.existsByMaSanPham(ma);
    }

    public Product luuSanPham(Product product) {
        return productRepository.save(product);
    }

    /**
     * Sửa thông tin sản phẩm. Mã sản phẩm và loại sản phẩm (lớp con) được giữ nguyên.
     * Các thuộc tính riêng của lớp con được cập nhật qua Factory.
     */
    public Product capNhatSanPham(Long id, String tenSanPham, String donViTinh, Double donGia,
                                  String ghiChu, Map<String, String> thuocTinhRieng) {
        Product product = laySanPhamTheoId(id);

        String ten = tenSanPham == null ? "" : tenSanPham.trim();
        if (ten.isEmpty()) {
            throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
        }
        if (donGia == null || donGia < 0) {
            throw new IllegalArgumentException("Đơn giá không được âm!");
        }
        product.setTenSanPham(ten);
        product.setDonViTinh(donViTinh == null || donViTinh.isBlank() ? "Cái" : donViTinh.trim());
        product.setDonGia(donGia);
        product.setGhiChu(ghiChu == null ? "" : ghiChu.trim());
        sanPhamFactory.capNhatThuocTinhRieng(product, thuocTinhRieng);
        return productRepository.save(product);
    }

    /**
     * Xóa sản phẩm - chỉ khi sản phẩm chưa phát sinh dữ liệu nào (lô hàng, phiếu nhập, phiếu xuất)
     * để không mất dấu vết kho. (Lưu ý: Product.loHangs để cascade ALL nên bắt buộc phải chặn ở đây.)
     */
    public void xoaSanPham(Long id) {
        Product product = laySanPhamTheoId(id);
        if (loHangRepository.existsByProduct_Id(id)) {
            throw new IllegalArgumentException("Không thể xóa '" + product.getTenSanPham()
                    + "' vì sản phẩm đã có lô hàng trong kho.");
        }
        if (chiTietPhieuNhapRepository.existsBySanPham_Id(id) || chiTietPhieuXuatRepository.existsBySanPham_Id(id)) {
            throw new IllegalArgumentException("Không thể xóa '" + product.getTenSanPham()
                    + "' vì sản phẩm đã xuất hiện trong phiếu nhập/xuất.");
        }
        productRepository.delete(product);
    }
}
