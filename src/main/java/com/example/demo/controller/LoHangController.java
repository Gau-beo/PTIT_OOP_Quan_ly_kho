package com.example.demo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.LoHang;
import com.example.demo.service.LoHangService;

@RestController
@RequestMapping("/api/lo-hang")
public class LoHangController {

    private final LoHangService loHangService;

    public LoHangController(LoHangService loHangService) {
        this.loHangService = loHangService;
    }

    @GetMapping
    public ResponseEntity<List<LoHang>> layTatCa() {
        return ResponseEntity.ok(loHangService.layTatCaLoHang());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoHang> layChiTiet(@PathVariable Long id) {
        return ResponseEntity.ok(loHangService.layLoHangTheoId(id));
    }

    @GetMapping("/tra-cuu")
    public ResponseEntity<List<LoHang>> traCuu(@RequestParam(required = false) String ma) {
        return ResponseEntity.ok(loHangService.traCuuLoHang(ma));
    }

    @GetMapping("/tra-cuu-nang-cao")
    public ResponseEntity<List<LoHang>> traCuuNangCao(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai) {
        return ResponseEntity.ok(loHangService.traCuuNangCao(keyword, trangThai));
    }

    @PostMapping
    public ResponseEntity<LoHang> themLoHang(@RequestBody LoHang loHang) {
        return ResponseEntity.status(HttpStatus.CREATED).body(loHangService.themLoHang(loHang));
    }
}
