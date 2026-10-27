package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ChiTietPhieuNhap;

public interface ChiTietPhieuNhapRepository extends JpaRepository<ChiTietPhieuNhap, Long> {

    // Dùng để chặn xóa sản phẩm đã xuất hiện trong phiếu nhập
    boolean existsBySanPham_Id(Long sanPhamId);
}
