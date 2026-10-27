package com.example.demo.repository;

import com.example.demo.entity.PhieuNhap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuNhapRepository extends JpaRepository<PhieuNhap, Long> {

    // Spring Data tự sinh câu truy vấn từ tên method: đếm phiếu nhập của một NCC
    // (dùng để chặn xóa NCC đã có phiếu nhập)
    boolean existsByNhaCungCap_Id(Integer nhaCungCapId);
}
