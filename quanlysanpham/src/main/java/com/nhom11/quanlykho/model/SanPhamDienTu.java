package com.nhom11.quanlykho.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("DIEN_TU")
@Getter
@Setter
@NoArgsConstructor
public class SanPhamDienTu extends SanPham {

    @Column(name = "thoi_gian_bao_hanh")
    private Integer thoiGianBaoHanh; // đơn vị: tháng

    @Column(name = "cong_suat")
    private Double congSuat; // đơn vị: watt

    public SanPhamDienTu(String tenSanPham, String donViTinh, Integer soLuongTon, Integer nguongCanhBao,
                          DanhMuc danhMuc, Integer thoiGianBaoHanh, Double congSuat) {
        super(tenSanPham, donViTinh, soLuongTon, nguongCanhBao, danhMuc);
        this.thoiGianBaoHanh = thoiGianBaoHanh;
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
