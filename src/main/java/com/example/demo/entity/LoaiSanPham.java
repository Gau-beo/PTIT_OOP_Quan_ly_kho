package com.example.demo.entity;

public enum LoaiSanPham {
    THUC_PHAM("Thực phẩm"),
    DE_VO("Hàng dễ vỡ"),
    HOA_CHAT("Hóa chất"),
    DIEN_TU("Điện tử"),
    CONG_KENH("Hàng cồng kềnh");

    private final String tenHienThi;

    LoaiSanPham(String tenHienThi) {
        this.tenHienThi = tenHienThi;
    }

    public String getTenHienThi() {
        return tenHienThi;
    }
}
