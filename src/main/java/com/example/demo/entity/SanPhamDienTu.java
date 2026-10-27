package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DIEN_TU")
public class SanPhamDienTu extends Product {

    @Column(name = "thoi_gian_bao_hanh")
    private Integer thoiGianBaoHanh;

    @Column(name = "cong_suat")
    private Double congSuat;

    public SanPhamDienTu() {
    }

    public SanPhamDienTu(String maSp, String tenSanPham, String donViTinh, Double donGia, String ghiChu) {
        super(maSp, tenSanPham, donViTinh, donGia, ghiChu);
    }

    public Integer getThoiGianBaoHanh() {
        return thoiGianBaoHanh;
    }

    public void setThoiGianBaoHanh(Integer thoiGianBaoHanh) {
        this.thoiGianBaoHanh = thoiGianBaoHanh;
    }

    public Double getCongSuat() {
        return congSuat;
    }

    public void setCongSuat(Double congSuat) {
        this.congSuat = congSuat;
    }

    @Override
    public String getThongTinRieng() {
        return "Bảo hành: " + (thoiGianBaoHanh != null ? thoiGianBaoHanh + " tháng" : "chưa cập nhật")
                + ", Công suất: " + (congSuat != null ? congSuat + "W" : "chưa cập nhật");
    }

    @Override
    public LoaiSanPham getLoaiSanPham() {
        return LoaiSanPham.DIEN_TU;
    }
}