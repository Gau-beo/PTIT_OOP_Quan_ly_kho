package com.example.demo.entity;

import jakarta.persistence.Entity;

@Entity
public class TaiKhoanQuanLy extends NguoiDung {

    public TaiKhoanQuanLy() {
    }

    public TaiKhoanQuanLy(String hoTen, String email,
                          String matKhau, String vaiTro) {
        super(hoTen, email, matKhau, vaiTro);
    }

    @Override
    public String getMoTaTaiKhoan() {
        return "Tài khoản Quản lý";
    }
}
