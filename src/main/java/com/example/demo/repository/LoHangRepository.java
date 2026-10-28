package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.LoHang;

public interface LoHangRepository extends JpaRepository<LoHang, Long> {

    boolean existsByMaLoHang(String maLoHang);

    boolean existsByProduct_Id(Long productId);

    List<LoHang> findByHanSuDungBeforeAndSoLuongGreaterThan(LocalDate ngay, int soLuong);

    List<LoHang> findByHanSuDungBetweenAndSoLuongGreaterThan(LocalDate tu, LocalDate den, int soLuong);

    @Query("SELECT lh FROM LoHang lh WHERE " +
           "(:keyword IS NULL OR :keyword = '' OR LOWER(lh.maLoHang) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(lh.product.tenSanPham) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(COALESCE(lh.nhaCungCap, '')) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
           "ORDER BY lh.ngayNhap DESC")
    List<LoHang> traCuu(@Param("keyword") String keyword);
}
