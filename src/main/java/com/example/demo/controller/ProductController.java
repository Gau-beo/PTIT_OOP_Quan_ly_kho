package com.example.demo.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/thong-ke")
    public ResponseEntity<Map<String, Object>> layThongKe() {
        long tongSo = productRepository.countTotalProducts();
        return ResponseEntity.ok(Map.of("tongSoSanPham", tongSo));
    }

    @PutMapping("/{id}/ghi-chu")
    public ResponseEntity<Product> capNhatGhiChu(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setGhiChu(body.get("ghiChu"));
        return ResponseEntity.ok(productRepository.save(product));
    }
}
