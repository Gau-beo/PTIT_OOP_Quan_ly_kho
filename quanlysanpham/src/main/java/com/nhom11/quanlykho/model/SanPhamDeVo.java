package com.nhom11.quanlykho.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("DE_VO")
@Getter
@Setter
@NoArgsConstructor
public class SanPhamDeVo extends SanPham {

    // Ví dụ giá trị: THAP, TRUNG_BINH, CAO
    @Column(name = "muc_do_de_vo")
    private String mucDoDeVo;

    @Column(name = "yeu_cau_dong_goi_dac_biet")
    private Boolean yeuCauDongGoiDacBiet = false;

    public SanPhamDeVo(String tenSanPham, String donViTinh, Integer soLuongTon, Integer nguongCanhBao,
                        DanhMuc danhMuc, String mucDoDeVo, Boolean yeuCauDongGoiDacBiet) {
        super(tenSanPham, donViTinh, soLuongTon, nguongCanhBao, danhMuc);
        this.mucDoDeVo = mucDoDeVo;
        this.yeuCauDongGoiDacBiet = yeuCauDongGoiDacBiet;
    }

    @Override
    public String getThongTinRieng() {
        return "Mức độ dễ vỡ: " + (mucDoDeVo != null ? mucDoDeVo : "chưa xác định")
                + (Boolean.TRUE.equals(yeuCauDongGoiDacBiet) ? " - Yêu cầu đóng gói đặc biệt" : "");
    }

    @Override
    public LoaiSanPham getLoaiSanPham() {
        return LoaiSanPham.DE_VO;
    }
}
