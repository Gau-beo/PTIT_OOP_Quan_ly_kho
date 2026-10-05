package com.nhom11.quanlykho.dto;

import com.nhom11.quanlykho.model.LoaiSanPham;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO dùng chung cho cả 5 loại sản phẩm khi tạo mới / cập nhật.
 * Field nào không thuộc loại sản phẩm đang tạo thì bỏ trống (null),
 * SanPhamFactory sẽ chỉ đọc field tương ứng với "loaiSanPham".
 */
@Getter
@Setter
public class SanPhamRequestDTO {

    @NotNull(message = "Vui lòng chọn loại sản phẩm")
    private LoaiSanPham loaiSanPham;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String tenSanPham;

    private String donViTinh;

    @NotNull(message = "Số lượng tồn không được để trống")
    private Integer soLuongTon;

    @NotNull(message = "Ngưỡng cảnh báo không được để trống")
    private Integer nguongCanhBao;

    private Integer danhMucId;

    // Riêng cho THUC_PHAM
    private LocalDate ngayHetHan;

    // Riêng cho DE_VO
    private String mucDoDeVo;
    private Boolean yeuCauDongGoiDacBiet;

    // Riêng cho HOA_CHAT
    private String mucDoRuiRo;

    // Riêng cho DIEN_TU
    private Integer thoiGianBaoHanh;
    private Double congSuat;

    // Riêng cho CONG_KENH
    private Double phuPhiVanChuyen;
}
