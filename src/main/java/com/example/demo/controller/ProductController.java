package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Product;
import com.example.demo.service.LoHangService;
import com.example.demo.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final LoHangService loHangService;

    public ProductController(ProductService productService, LoHangService loHangService) {
        this.productService = productService;
        this.loHangService = loHangService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> layTatCaSanPham() {
        return ResponseEntity.ok(productService.layTatCaSanPham());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> layChiTietSanPham(@PathVariable Long id) {
        return ResponseEntity.ok(productService.laySanPhamTheoId(id));
    }

    @PostMapping
    public ResponseEntity<Product> themSanPham(@RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.luuSanPham(product));
    }

    @GetMapping("/thong-ke")
    public ResponseEntity<Map<String, Object>> layThongKe() {
        long tongSoSanPham = productService.layTongSoSanPham();
        long tongSoLoHang = loHangService.layTongSoLoHang();
        long tongTonKho = productService.layTongTonKho();
        int loHangSapHetHan = loHangService.layLoHangSapHetHan().size();
        int loHangHetHan = loHangService.layLoHangHetHan().size();

        Map<String, Object> thongKe = new HashMap<>();
        thongKe.put("tongSoSanPham", tongSoSanPham);
        thongKe.put("tongSoLoHang", tongSoLoHang);
        thongKe.put("tongTonKho", tongTonKho);
        thongKe.put("loHangSapHetHan", loHangSapHetHan);
        thongKe.put("loHangHetHan", loHangHetHan);

        return ResponseEntity.ok(thongKe);
    }

    @PutMapping("/{id}/ghi-chu")
    public ResponseEntity<Product> capNhatGhiChu(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(productService.capNhatGhiChu(id, body));
    }
}
