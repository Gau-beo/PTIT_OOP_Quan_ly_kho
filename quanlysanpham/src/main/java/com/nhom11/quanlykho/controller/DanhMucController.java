package com.nhom11.quanlykho.controller;

import com.nhom11.quanlykho.model.DanhMuc;
import com.nhom11.quanlykho.service.DanhMucService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/danh-muc")
@RequiredArgsConstructor
public class DanhMucController {

    private final DanhMucService danhMucService;

    @GetMapping
    public ResponseEntity<List<DanhMuc>> layTatCa() {
        return ResponseEntity.ok(danhMucService.layTatCa());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DanhMuc> layTheoId(@PathVariable Integer id) {
        return ResponseEntity.ok(danhMucService.layTheoId(id));
    }

    @PostMapping
    public ResponseEntity<DanhMuc> taoMoi(@RequestBody Map<String, String> body) {
        DanhMuc danhMuc = danhMucService.taoMoi(body.get("tenDanhMuc"));
        return ResponseEntity.status(HttpStatus.CREATED).body(danhMuc);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DanhMuc> capNhat(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(danhMucService.capNhat(id, body.get("tenDanhMuc")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> xoa(@PathVariable Integer id) {
        danhMucService.xoa(id);
        return ResponseEntity.noContent().build();
    }
}
