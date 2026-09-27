package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public long layTongSoSanPham() {
        return productRepository.countTotalProducts();
    }

    public Product capNhatGhiChu(Long id, Map<String, String> body) {
        Product product = productRepository.findById(id).orElseThrow();
        product.setGhiChu(body.get("ghiChu"));
        return productRepository.save(product);
    }
}
