package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.ProductRepository;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> layTatCaSanPham() {
        return productRepository.findAll();
    }

    public Product laySanPhamTheoId(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có ID: " + id));
    }

    public long layTongSoSanPham() {
        return productRepository.countTotalProducts();
    }

    public long layTongTonKho() {
        return productRepository.sumTotalInventory();
    }

    public boolean tonTaiMaSanPham(String ma) {
        return productRepository.existsByMaSanPham(ma);
    }

    public Product luuSanPham(Product product) {
        return productRepository.save(product);
    }

    public Product capNhatGhiChu(Long id, Map<String, String> body) {
        String ghiChu = body != null ? body.get("ghiChu") : null;
        return capNhatGhiChuText(id, ghiChu);
    }

    public Product capNhatGhiChuText(Long id, String ghiChu) {
        Product product = laySanPhamTheoId(id);
        product.setGhiChu(ghiChu);
        return productRepository.save(product);
    }
}
