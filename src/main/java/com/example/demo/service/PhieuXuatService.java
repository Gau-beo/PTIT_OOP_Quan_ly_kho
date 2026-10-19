package com.example.demo.service;

import com.example.demo.dto.ChiTietPhieuXuatRequest;
import com.example.demo.dto.PhieuXuatRequest;
import com.example.demo.entity.LoHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.PhieuXuat;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.PhieuXuatRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class PhieuXuatService {

    private final PhieuXuatRepository phieuXuatRepository;
    private final ProductRepository productRepository;
    private final NguoiDungRepository nguoiDungRepository;

    // Sử dụng Constructor Injection giống như ProductService
    public PhieuXuatService(PhieuXuatRepository phieuXuatRepository,
                            ProductRepository productRepository,
                            NguoiDungRepository nguoiDungRepository) {
        this.phieuXuatRepository = phieuXuatRepository;
        this.productRepository = productRepository;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    public PhieuXuat taoPhieuXuat(PhieuXuatRequest request) {
        // 1. Kiểm tra người dùng tạo phiếu
        NguoiDung nguoiTao = nguoiDungRepository.findById(request.getNguoiTaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng ID: " + request.getNguoiTaoId()));

        // 2. Kiểm tra tồn kho và trừ số lượng sản phẩm theo Lô hàng (FIFO)
        if (request.getChiTietList() != null && !request.getChiTietList().isEmpty()) {
            for (ChiTietPhieuXuatRequest item : request.getChiTietList()) {
                Product product = productRepository.findById(item.getSanPhamId())
                        .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm ID: " + item.getSanPhamId()));

                // Kiểm tra tổng tồn kho
                if (product.getTongTonKho() < item.getSoLuong()) {
                    throw new IllegalArgumentException("Sản phẩm '" + product.getTenSanPham() + "' không đủ số lượng tồn kho!");
                }

                // Logic trừ tồn kho theo từng lô hàng (FIFO)
                int soLuongCanXuat = item.getSoLuong();
                if (product.getLoHangs() != null) {
                    for (LoHang loHang : product.getLoHangs()) {
                        if (soLuongCanXuat <= 0) break;

                        int soLuongHienTai = loHang.getSoLuong();
                        if (soLuongHienTai > 0) {
                            if (soLuongHienTai >= soLuongCanXuat) {
                                loHang.setSoLuong(soLuongHienTai - soLuongCanXuat);
                                soLuongCanXuat = 0;
                            } else {
                                soLuongCanXuat -= soLuongHienTai;
                                loHang.setSoLuong(0);
                            }
                        }
                    }
                }

                // Kiểm tra xem đã trừ hết số lượng cần xuất chưa
                if (soLuongCanXuat > 0) {
                    throw new IllegalArgumentException("Các lô hàng của sản phẩm '" + product.getTenSanPham() + "' không đủ để xuất!");
                }

                productRepository.save(product);
            }
        }

        // 3. Tạo phiếu xuất mới
        PhieuXuat phieuXuat = new PhieuXuat();
        phieuXuat.setNguoiNhan(request.getNguoiNhan());
        phieuXuat.setNguoiTao(nguoiTao);
        phieuXuat.setNgayXuat(LocalDateTime.now());

        return phieuXuatRepository.save(phieuXuat);
    }
}