package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("THUC_PHAM")
public class SanPhamThucPham extends Product {

    @Column(name = "ngay_het_han")
    private LocalDate ngayHetHan;

    public SanPhamThucPham() {
    }

    public SanPhamThucPham(String maSp, String tenSanPham, String donViTinh, Double donGia, String ghiChu) {
        super(maSp, tenSanPham, donViTinh, donGia, ghiChu);
    }

    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(LocalDate ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public boolean daHetHan() {
        return ngayHetHan != null && ngayHetHan.isBefore(LocalDate.now());
    }

    @Override
    public String getThongTinRieng() {
        return "Hạn sử dụng: " + (ngayHetHan != null ? ngayHetHan : "chưa cập nhật")
                + (daHetHan() ? " (ĐÃ HẾT HẠN)" : "");
    }

    @Override
    public LoaiSanPham getLoaiSanPham() {
        return LoaiSanPham.THUC_PHAM;
    }
}
