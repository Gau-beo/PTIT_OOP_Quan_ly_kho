package com.example.demo.controller;

import com.example.demo.entity.Product;
import com.example.demo.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/thong-ke")
    public ResponseEntity<Map<String, Object>> layThongKe() {
        long tongSo = productService.layTongSoSanPham();
        return ResponseEntity.ok(Map.of("tongSoSanPham", tongSo));
    }

    @PutMapping("/{id}/ghi-chu")
    public ResponseEntity<Product> capNhatGhiChu(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(productService.capNhatGhiChu(id, body));
    }
}
