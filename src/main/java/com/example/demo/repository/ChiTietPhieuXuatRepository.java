package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ChiTietPhieuXuat;

public interface ChiTietPhieuXuatRepository extends JpaRepository<ChiTietPhieuXuat, Long> {

    boolean existsBySanPham_Id(Long sanPhamId);
}
