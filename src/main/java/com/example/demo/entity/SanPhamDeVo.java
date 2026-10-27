package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DE_VO")
public class SanPhamDeVo extends Product {

    @Column(name = "muc_do_de_vo")
    private String mucDoDeVo;

    @Column(name = "yeu_cau_dong_goi_dac_biet")
    private Boolean yeuCauDongGoiDacBiet = false;

    public SanPhamDeVo() {
    }

    public SanPhamDeVo(String maSp, String tenSanPham, String donViTinh, Double donGia, String ghiChu) {
        super(maSp, tenSanPham, donViTinh, donGia, ghiChu);
    }

    public String getMucDoDeVo() {
        return mucDoDeVo;
    }

    public void setMucDoDeVo(String mucDoDeVo) {
        this.mucDoDeVo = mucDoDeVo;
    }

    public Boolean getYeuCauDongGoiDacBiet() {
        return yeuCauDongGoiDacBiet;
    }

    public void setYeuCauDongGoiDacBiet(Boolean yeuCauDongGoiDacBiet) {
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