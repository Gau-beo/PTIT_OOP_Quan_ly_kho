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
