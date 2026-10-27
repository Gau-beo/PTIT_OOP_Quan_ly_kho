package com.example.demo.repository;

import com.example.demo.entity.NhaCungCap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NhaCungCapRepository extends JpaRepository<NhaCungCap, Integer> {

    // Kiểm tra trùng tên (không phân biệt hoa thường) khi thêm mới
    boolean existsByTenNCCIgnoreCase(String tenNCC);

    // Kiểm tra trùng tên khi sửa (loại trừ chính bản ghi đang sửa)
    boolean existsByTenNCCIgnoreCaseAndIdNot(String tenNCC, Integer id);
}
