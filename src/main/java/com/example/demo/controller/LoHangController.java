package com.example.demo.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.LoHang;
import com.example.demo.repository.LoHangRepository;

@RestController
@RequestMapping("/api/lo-hang")
public class LoHangController {

    private final LoHangRepository loHangRepository;

    public LoHangController(LoHangRepository loHangRepository) {
        this.loHangRepository = loHangRepository;
    }

    @GetMapping("/tra-cuu")
    public ResponseEntity<List<LoHang>> traCuu(@RequestParam String ma) {
        return ResponseEntity.ok(loHangRepository.findByMaLoHangContaining(ma));
    }
}
