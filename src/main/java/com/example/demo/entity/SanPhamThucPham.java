package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Sản phẩm thực phẩm. Hạn dùng cụ thể được quản lý theo TỪNG LÔ (LoHang.hanSuDung),
 * còn ở cấp sản phẩm chỉ lưu "thời hạn sử dụng tiêu chuẩn" (số tháng) để tham khảo.
 */
@Entity
@DiscriminatorValue("THUC_PHAM")
public class SanPhamThucPham extends Product {

    @Column(name = "thoi_han_su_dung_thang")
    private Integer thoiHanSuDungThang;

    public SanPhamThucPham() {
    }

    public SanPhamThucPham(String maSp, String tenSanPham, String donViTinh, Double donGia, String ghiChu) {
        super(maSp, tenSanPham, donViTinh, donGia, ghiChu);
    }

    public Integer getThoiHanSuDungThang() {
        return thoiHanSuDungThang;
    }

    public void setThoiHanSuDungThang(Integer thoiHanSuDungThang) {
        this.thoiHanSuDungThang = thoiHanSuDungThang;
    }

    @Override
    public String getThongTinRieng() {
        return "Thời hạn sử dụng: "
                + (thoiHanSuDungThang != null ? thoiHanSuDungThang + " tháng" : "chưa cập nhật");
    }

    @Override
    public LoaiSanPham getLoaiSanPham() {
        return LoaiSanPham.THUC_PHAM;
    }
}
