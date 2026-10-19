package com.example.demo.controller;

import com.example.demo.dto.PhieuXuatRequest;
import com.example.demo.entity.PhieuXuat;
import com.example.demo.service.PhieuXuatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phieu-xuat")
@CrossOrigin(origins = "*") // Cho phép Frontend (React/Vue/Angular/HTML) gọi API không bị chặn CORS
public class PhieuXuatController {

    private final PhieuXuatService phieuXuatService;

    // Constructor Injection đồng bộ với Service
    public PhieuXuatController(PhieuXuatService phieuXuatService) {
        this.phieuXuatService = phieuXuatService;
    }

    @PostMapping
    public ResponseEntity<?> taoPhieuXuat(@RequestBody PhieuXuatRequest request) {
        try {
            PhieuXuat phieuXuat = phieuXuatService.taoPhieuXuat(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(phieuXuat);
        } catch (IllegalArgumentException e) {
            // Lỗi liên quan đến logic (ví dụ: không đủ số lượng tồn kho)
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Lỗi hệ thống hoặc lỗi khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi tạo phiếu xuất: " + e.getMessage());
        }
    }
}