package com.example.demo.service;

import com.example.demo.entity.LoHang;
import com.example.demo.repository.LoHangRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoHangService {

    private final LoHangRepository loHangRepository;

    public LoHangService(LoHangRepository loHangRepository) {
        this.loHangRepository = loHangRepository;
    }

    public List<LoHang> traCuuLoHang(String ma) {
        return loHangRepository.findByMaLoHangContaining(ma);
    }
}
