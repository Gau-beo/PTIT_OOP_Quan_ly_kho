package com.nhom11.quanlykho.dto;

import com.nhom11.quanlykho.model.LoaiSanPham;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SanPhamResponseDTO {
    private Integer id;
    private LoaiSanPham loaiSanPham;
    private String tenSanPham;
    private String donViTinh;
    private Integer soLuongTon;
    private Integer nguongCanhBao;
    private Integer danhMucId;
    private String tenDanhMuc;
    private String thongTinRieng;   // do đa hình sinh ra, khác nhau tùy loại
    private boolean canCanhBaoTonKho;
}
