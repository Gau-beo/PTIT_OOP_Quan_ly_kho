package com.example.demo.repository;

import com.example.demo.entity.PhieuXuat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhieuXuatRepository extends JpaRepository<PhieuXuat, Long> {

    boolean existsByNguoiTao_Id(Long nguoiTaoId);
}
