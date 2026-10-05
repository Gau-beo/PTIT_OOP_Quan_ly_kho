package com.nhom11.quanlykho.service;

import com.nhom11.quanlykho.dto.SanPhamRequestDTO;
import com.nhom11.quanlykho.dto.SanPhamResponseDTO;

import java.util.List;

public interface SanPhamService {
    SanPhamResponseDTO taoMoi(SanPhamRequestDTO dto);
    SanPhamResponseDTO capNhat(Integer id, SanPhamRequestDTO dto);
    void xoa(Integer id);
    SanPhamResponseDTO layTheoId(Integer id);
    List<SanPhamResponseDTO> layTatCa();
    List<SanPhamResponseDTO> layTheoDanhMuc(Integer danhMucId);
    List<SanPhamResponseDTO> layDanhSachCanhBaoTonKho();
}
