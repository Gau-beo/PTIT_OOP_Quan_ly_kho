package com.nhom11.quanlykho.dto;

import com.nhom11.quanlykho.model.LoaiSanPham;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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

    // Thuộc tính riêng từng loại (chỉ có giá trị ở đúng loại, loại khác để null).
    // Frontend dùng các trường này để điền sẵn vào form Sửa.
    private LocalDate ngayHetHan;            // THUC_PHAM
    private String mucDoDeVo;                // DE_VO
    private Boolean yeuCauDongGoiDacBiet;    // DE_VO
    private String mucDoRuiRo;               // HOA_CHAT
    private Integer thoiGianBaoHanh;         // DIEN_TU
    private Double congSuat;                 // DIEN_TU
    private Double phuPhiVanChuyen;          // CONG_KENH
}
