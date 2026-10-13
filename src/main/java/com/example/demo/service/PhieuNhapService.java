package com.example.demo.service;

import com.example.demo.entity.PhieuNhap;
import com.example.demo.repository.PhieuNhapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PhieuNhapService {

    @Autowired
    private PhieuNhapRepository phieuNhapRepository;

    public List<PhieuNhap> getAllPhieuNhap() {
        return phieuNhapRepository.findAll();
    }

    public PhieuNhap savePhieuNhap(PhieuNhap phieuNhap) {
        return phieuNhapRepository.save(phieuNhap);
    }

    public PhieuNhap getPhieuNhapById(Long id) {
        Optional<PhieuNhap> phieuNhap = phieuNhapRepository.findById(id);
        return phieuNhap.orElse(null);
    }

    public void deletePhieuNhap(Long id) {
        phieuNhapRepository.deleteById(id);
    }
}