package com.example.demo.controller;

import com.example.demo.entity.LoHang;
import com.example.demo.service.LoHangService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lo-hang")
public class LoHangController {

    private final LoHangService loHangService;

    public LoHangController(LoHangService loHangService) {
        this.loHangService = loHangService;
    }

    @GetMapping("/tra-cuu")
    public ResponseEntity<List<LoHang>> traCuu(@RequestParam String ma) {
        return ResponseEntity.ok(loHangService.traCuuLoHang(ma));
    }
}
    public ResponseEntity<List<LoHang>> traCuu(@RequestParam String ma) {
        return ResponseEntity.ok(loHangRepository.findByMaLoHangContaining(ma));
    }
}
