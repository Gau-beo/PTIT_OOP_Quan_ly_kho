package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ChiTietPhieuXuat;

public interface ChiTietPhieuXuatRepository extends JpaRepository<ChiTietPhieuXuat, Long> {

    // Dùng để chặn xóa sản phẩm đã xuất hiện trong phiếu xuất
    boolean existsBySanPham_Id(Long sanPhamId);
}
