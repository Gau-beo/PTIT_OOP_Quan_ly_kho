package com.example.demo.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ChiTietPhieuNhapRequest;
import com.example.demo.entity.ChiTietPhieuNhap;
import com.example.demo.entity.LoHang;
import com.example.demo.entity.NhaCungCap;
import com.example.demo.entity.PhieuNhap;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoHangRepository;
import com.example.demo.repository.NhaCungCapRepository;
import com.example.demo.repository.PhieuNhapRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;
    private final LoHangRepository loHangRepository;
    private final ProductRepository productRepository;
    private final NhaCungCapRepository nhaCungCapRepository;

    public PhieuNhapService(PhieuNhapRepository phieuNhapRepository,
                            LoHangRepository loHangRepository,
                            ProductRepository productRepository,
                            NhaCungCapRepository nhaCungCapRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
        this.loHangRepository = loHangRepository;
        this.productRepository = productRepository;
        this.nhaCungCapRepository = nhaCungCapRepository;
    }

    @Transactional(readOnly = true)
    public List<PhieuNhap> layTatCa() {
        return phieuNhapRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Tạo phiếu nhập: lưu phiếu + chi tiết, đồng thời sinh một lô hàng mới cho mỗi dòng
     * (số lượng tồn kho của sản phẩm chính là tổng số lượng các lô).
     *
     * SỬA: nhà cung cấp giờ bắt buộc phải là một NCC có thật trong bảng nha_cung_cap (truyền vào bằng id).
     */
    @Transactional
    public PhieuNhap taoPhieuNhap(Integer nhaCungCapId, List<ChiTietPhieuNhapRequest> dong) {
        if (nhaCungCapId == null) {
            throw new IllegalArgumentException("Vui lòng chọn nhà cung cấp!");
        }
        NhaCungCap ncc = nhaCungCapRepository.findById(nhaCungCapId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp ID: " + nhaCungCapId));

        if (dong == null || dong.isEmpty()) {
            throw new IllegalArgumentException("Phiếu nhập phải có ít nhất một dòng sản phẩm!");
        }

        LocalDate homNay = LocalDate.now();
        PhieuNhap phieuNhap = new PhieuNhap();
        phieuNhap.setNgayNhap(new Date());
        phieuNhap.setNhaCungCap(ncc);
        phieuNhap.setChiTietPhieuNhaps(new ArrayList<>());

        double tongTien = 0;
        for (ChiTietPhieuNhapRequest r : dong) {
            if (r.getSoLuong() <= 0) {
                throw new IllegalArgumentException("Số lượng nhập phải lớn hơn 0!");
            }
            if (r.getDonGiaNhap() < 0) {
                throw new IllegalArgumentException("Đơn giá nhập không được âm!");
            }
            if (r.getHanSuDung() != null && r.getHanSuDung().isBefore(homNay)) {
                throw new IllegalArgumentException("Hạn sử dụng không được trước ngày nhập kho!");
            }
            Product product = productRepository.findById(r.getSanPhamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm ID: " + r.getSanPhamId()));

            ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
            ct.setPhieuNhap(phieuNhap);
            ct.setSanPham(product);
            ct.setSoLuong(r.getSoLuong());
            ct.setDonGiaNhap(r.getDonGiaNhap());
            ct.setHanSuDung(r.getHanSuDung());
            phieuNhap.getChiTietPhieuNhaps().add(ct);

            tongTien += r.getSoLuong() * r.getDonGiaNhap();
        }
        phieuNhap.setTongTien(tongTien);

        PhieuNhap daLuu = phieuNhapRepository.save(phieuNhap);

        // LoHang vẫn lưu tên NCC dạng chuỗi (ảnh chụp tại thời điểm nhập),
        // nên ô tìm kiếm theo NCC ở trang Tra cứu lô hàng vẫn hoạt động như cũ.
        int stt = 1;
        for (ChiTietPhieuNhap ct : daLuu.getChiTietPhieuNhaps()) {
            String maLo = taoMaLoDuyNhat("LH-PN" + daLuu.getId() + "-" + stt++);
            loHangRepository.save(new LoHang(maLo, homNay, ct.getHanSuDung(), ct.getSoLuong(),
                    ncc.getTenNCC(), ct.getSanPham()));
        }
        return daLuu;
    }

    /** Nếu mã lô tự sinh trùng với một lô đã nhập tay thì thêm hậu tố -2, -3... cho đến khi duy nhất. */
    private String taoMaLoDuyNhat(String maGoc) {
        String ma = maGoc;
        int hauTo = 2;
        while (loHangRepository.existsByMaLoHang(ma)) {
            ma = maGoc + "-" + hauTo++;
        }
        return ma;
    }
}
