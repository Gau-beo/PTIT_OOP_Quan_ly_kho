package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.LoHang;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoHangRepository;

@Service
@Transactional
public class LoHangService {

    private final LoHangRepository loHangRepository;

    public LoHangService(LoHangRepository loHangRepository) {
        this.loHangRepository = loHangRepository;
    }

    public List<LoHang> layTatCaLoHang() {
        return loHangRepository.findAll();
    }

    public LoHang layLoHangTheoId(Long id) {
        return loHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lô hàng có ID: " + id));
    }

    public List<LoHang> traCuuLoHang(String ma) {
        if (ma == null || ma.isBlank()) {
            return loHangRepository.findAll();
        }
        return loHangRepository.findByMaLoHangContaining(ma.trim());
    }

    public List<LoHang> traCuuNangCao(String keyword, String trangThai) {
        List<LoHang> danhSach = loHangRepository.traCuu(keyword != null ? keyword.trim() : "");
        
        if (trangThai == null || trangThai.isBlank() || "tat_ca".equalsIgnoreCase(trangThai)) {
            return danhSach;
        }

        return danhSach.stream().filter(lh -> {
            if ("con_han".equalsIgnoreCase(trangThai)) {
                return !lh.isHetHan() && !lh.isSapHetHan();
            } else if ("sap_het_han".equalsIgnoreCase(trangThai)) {
                return lh.isSapHetHan();
            } else if ("het_han".equalsIgnoreCase(trangThai)) {
                return lh.isHetHan();
            }
            return true;
        }).collect(Collectors.toList());
    }

    public LoHang themLoHang(LoHang loHang) {
        // SỬA: trước đây lưu thẳng, không kiểm tra gì -> có thể nhập số lượng âm,
        // hoặc trùng mã lô làm DB ném lỗi kỹ thuật (unique constraint) ra màn hình.
        String ma = loHang.getMaLoHang() == null ? "" : loHang.getMaLoHang().trim();
        if (ma.isEmpty()) {
            throw new IllegalArgumentException("Mã lô hàng không được để trống!");
        }
        if (loHangRepository.existsByMaLoHang(ma)) {
            throw new IllegalArgumentException("Mã lô hàng '" + ma + "' đã tồn tại!");
        }
        if (loHang.getProduct() == null) {
            throw new IllegalArgumentException("Vui lòng chọn sản phẩm cho lô hàng!");
        }
        if (loHang.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng lô hàng phải lớn hơn 0!");
        }
        if (loHang.getNgayNhap() == null) {
            throw new IllegalArgumentException("Ngày nhập không được để trống!");
        }
        if (loHang.getHanSuDung() != null && loHang.getHanSuDung().isBefore(loHang.getNgayNhap())) {
            throw new IllegalArgumentException("Hạn sử dụng không được trước ngày nhập!");
        }
        loHang.setMaLoHang(ma);
        return loHangRepository.save(loHang);
    }

    public long layTongSoLoHang() {
        return loHangRepository.count();
    }

    public List<LoHang> layLoHangHetHan() {
        return loHangRepository.findByHanSuDungBefore(LocalDate.now());
    }

    public List<LoHang> layLoHangSapHetHan() {
        LocalDate now = LocalDate.now();
        LocalDate in30Days = now.plusDays(30);
        return loHangRepository.findByHanSuDungBetween(now, in30Days);
    }
}
