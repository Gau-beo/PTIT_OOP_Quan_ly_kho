package com.nhom11.quanlykho.repository;

import com.nhom11.quanlykho.model.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    List<SanPham> findByDanhMuc_Id(Integer danhMucId);

    boolean existsByDanhMuc_Id(Integer danhMucId);

    // Lấy tất cả sản phẩm (không phân biệt loại con) đang chạm/dưới ngưỡng cảnh báo
    @Query("SELECT s FROM SanPham s WHERE s.soLuongTon <= s.nguongCanhBao")
    List<SanPham> timSanPhamCanCanhBao();
}
