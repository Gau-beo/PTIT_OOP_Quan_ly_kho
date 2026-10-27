package com.example.demo.entity;

/**
 * Liệt kê các loại sản phẩm con trong cây kế thừa của Product.
 */
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
