package com.nhom11.quanlykho.controller;

import com.nhom11.quanlykho.dto.SanPhamRequestDTO;
import com.nhom11.quanlykho.dto.SanPhamResponseDTO;
import com.nhom11.quanlykho.service.SanPhamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/san-pham")
@RequiredArgsConstructor
public class SanPhamController {

    private final SanPhamService sanPhamService;

    @GetMapping
    public ResponseEntity<List<SanPhamResponseDTO>> layTatCa() {
        return ResponseEntity.ok(sanPhamService.layTatCa());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanPhamResponseDTO> layTheoId(@PathVariable Integer id) {
        return ResponseEntity.ok(sanPhamService.layTheoId(id));
    }

    @GetMapping("/danh-muc/{danhMucId}")
    public ResponseEntity<List<SanPhamResponseDTO>> layTheoDanhMuc(@PathVariable Integer danhMucId) {
        return ResponseEntity.ok(sanPhamService.layTheoDanhMuc(danhMucId));
    }

    /**
     * Danh sách sản phẩm đang cần cảnh báo tồn kho (soLuongTon <= nguongCanhBao),
     * áp dụng chung cho cả 5 loại sản phẩm con.
     */
    @GetMapping("/canh-bao-ton-kho")
    public ResponseEntity<List<SanPhamResponseDTO>> layDanhSachCanhBaoTonKho() {
        return ResponseEntity.ok(sanPhamService.layDanhSachCanhBaoTonKho());
    }

    @PostMapping
    public ResponseEntity<SanPhamResponseDTO> taoMoi(@Valid @RequestBody SanPhamRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sanPhamService.taoMoi(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanPhamResponseDTO> capNhat(@PathVariable Integer id,
                                                       @Valid @RequestBody SanPhamRequestDTO dto) {
        return ResponseEntity.ok(sanPhamService.capNhat(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> xoa(@PathVariable Integer id) {
        sanPhamService.xoa(id);
        return ResponseEntity.noContent().build();
    }
}
