package com.example.demo.entity;

import jakarta.persistence.Entity;

@Entity
public class TaiKhoanNguoiDung extends NguoiDung {

    private String diaChi;

    // Hàm tạo không tham số
    public TaiKhoanNguoiDung() {
        super();
    }

    // Hàm tạo có tham số
    public TaiKhoanNguoiDung(
            String hoTen,
            String email,
            String matKhau,
            String vaiTro,
            String diaChi) {

        super(hoTen, email, matKhau, vaiTro);
        this.diaChi = diaChi;
    }

    @Override
    public String getMoTaTaiKhoan() {
        return "Người dùng" + (diaChi != null ? " - " + diaChi : "");
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
}