package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    protected String hoTen;
    protected String email;
    protected String matKhau;
    protected String vaiTro;

    // Hàm tạo không tham số
    public NguoiDung() {
    }

    // Hàm tạo có tham số
    public NguoiDung(String hoTen, String email, String matKhau, String vaiTro) {
        this.hoTen = hoTen;
        this.email = email;
        this.matKhau = matKhau;
        this.vaiTro = vaiTro;
    }

    /** Đa hình: lớp con ghi đè để mô tả loại tài khoản. */
    public String getMoTaTaiKhoan() {
        return "Tài khoản";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
}