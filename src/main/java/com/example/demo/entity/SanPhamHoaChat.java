package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("HOA_CHAT")
public class SanPhamHoaChat extends Product {

    @Column(name = "muc_do_rui_ro")
    private String mucDoRuiRo;

    public SanPhamHoaChat() {
    }

    public SanPhamHoaChat(String maSp, String tenSanPham, String donViTinh, Double donGia, String ghiChu) {
        super(maSp, tenSanPham, donViTinh, donGia, ghiChu);
    }

    public String getMucDoRuiRo() {
        return mucDoRuiRo;
    }

    public void setMucDoRuiRo(String mucDoRuiRo) {
        this.mucDoRuiRo = mucDoRuiRo;
    }

    @Override
    public String getThongTinRieng() {
        return "Mức độ rủi ro: " + (mucDoRuiRo != null ? mucDoRuiRo : "chưa xác định");
    }

    @Override
    public LoaiSanPham getLoaiSanPham() {
        return LoaiSanPham.HOA_CHAT;
    }
}