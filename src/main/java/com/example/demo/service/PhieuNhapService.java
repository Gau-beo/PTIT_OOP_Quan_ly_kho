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
import com.example.demo.entity.PhieuNhap;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoHangRepository;
import com.example.demo.repository.PhieuNhapRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class PhieuNhapService {

    private final PhieuNhapRepository phieuNhapRepository;
    private final LoHangRepository loHangRepository;
    private final ProductRepository productRepository;

    public PhieuNhapService(PhieuNhapRepository phieuNhapRepository,
                            LoHangRepository loHangRepository,
                            ProductRepository productRepository) {
        this.phieuNhapRepository = phieuNhapRepository;
        this.loHangRepository = loHangRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<PhieuNhap> layTatCa() {
        return phieuNhapRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Tạo phiếu nhập: lưu phiếu + chi tiết, đồng thời sinh một lô hàng mới cho mỗi dòng
     * (số lượng tồn kho của sản phẩm chính là tổng số lượng các lô).
     */
    @Transactional
    public PhieuNhap taoPhieuNhap(String nhaCungCap, List<ChiTietPhieuNhapRequest> dong) {
        if (dong == null || dong.isEmpty()) {
            throw new IllegalArgumentException("Phiếu nhập phải có ít nhất một dòng sản phẩm!");
        }

        LocalDate homNay = LocalDate.now();
        PhieuNhap phieuNhap = new PhieuNhap();
        phieuNhap.setNgayNhap(new Date());
        phieuNhap.setNhaCungCap(nhaCungCap == null || nhaCungCap.isBlank() ? "Không rõ" : nhaCungCap.trim());
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

        int stt = 1;
        for (ChiTietPhieuNhap ct : daLuu.getChiTietPhieuNhaps()) {
            String maLo = "LH-PN" + daLuu.getId() + "-" + stt++;
            loHangRepository.save(new LoHang(maLo, homNay, ct.getHanSuDung(), ct.getSoLuong(),
                    daLuu.getNhaCungCap(), ct.getSanPham()));
        }
        return daLuu;
    }
}
