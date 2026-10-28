package com.example.demo.repository;

import com.example.demo.entity.NhaCungCap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {

    boolean existsByTenNCCIgnoreCase(String tenNCC);

    boolean existsByTenNCCIgnoreCaseAndIdNot(String tenNCC, Integer id);
}
