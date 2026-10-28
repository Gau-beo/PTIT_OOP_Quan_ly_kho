package com.example.demo.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByMaSanPham(String maSanPham);

    boolean existsByMaSanPham(String maSanPham);

    @Modifying
    @Transactional
    @Query(value = "UPDATE products SET loai_san_pham = 'THUC_PHAM' WHERE loai_san_pham = 'Product'", nativeQuery = true)
    int chuyenLoaiCu();

    @Query("SELECT COUNT(p) FROM Product p")
    long countTotalProducts();

    @Query("SELECT COALESCE(SUM(lh.soLuong), 0) FROM LoHang lh")
    long sumTotalInventory();

    @Query("SELECT COALESCE(SUM(lh.soLuong), 0) FROM LoHang lh WHERE lh.hanSuDung IS NULL OR lh.hanSuDung >= :homNay")
    long sumAvailableInventory(@Param("homNay") LocalDate homNay);
}
