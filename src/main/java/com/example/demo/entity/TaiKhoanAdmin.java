package com.example.demo.entity;

import jakarta.persistence.Entity;

@Entity
public class TaiKhoanAdmin extends NguoiDung {

    private String chucVu;

    // Hàm tạo không tham số
    public TaiKhoanAdmin() {
        super();
    }

    // Hàm tạo có tham số
    public TaiKhoanAdmin(
            String hoTen,
            String email,
            String matKhau,
            String vaiTro,
            String chucVu) {

        super(hoTen, email, matKhau, vaiTro);
        this.chucVu = chucVu;
    }

    @Override
    public String getMoTaTaiKhoan() {
        return "Quản trị viên" + (chucVu != null ? " - " + chucVu : "");
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
}