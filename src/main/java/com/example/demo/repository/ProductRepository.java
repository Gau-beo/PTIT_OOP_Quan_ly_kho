package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByMaSanPham(String maSanPham);

    List<Product> findByTenSanPhamContainingIgnoreCase(String tenSanPham);

    @Query("SELECT COUNT(p) FROM Product p")
    long countTotalProducts();

    @Query("SELECT COALESCE(SUM(lh.soLuong), 0) FROM LoHang lh")
    long sumTotalInventory();
}
