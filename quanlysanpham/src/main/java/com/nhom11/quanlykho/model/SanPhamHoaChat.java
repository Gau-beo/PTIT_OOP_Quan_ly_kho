package com.nhom11.quanlykho.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("HOA_CHAT")
@Getter
@Setter
@NoArgsConstructor
public class SanPhamHoaChat extends SanPham {

    // Ví dụ giá trị: THAP, TRUNG_BINH, CAO
    @Column(name = "muc_do_rui_ro")
    private String mucDoRuiRo;

    public SanPhamHoaChat(String tenSanPham, String donViTinh, Integer soLuongTon,
                           Integer nguongCanhBao, DanhMuc danhMuc, String mucDoRuiRo) {
        super(tenSanPham, donViTinh, soLuongTon, nguongCanhBao, danhMuc);
        this.mucDoRuiRo = mucDoRuiRo;
    }

    @Override
    public String getThongTinRieng() {
        return "Mức độ rủi ro: " + (mucDoRuiRo != null ? mucDoRuiRo : "chưa xác định");
    }

    @Override
    public LoaiSanPham getLoaiSanPham() {
        return LoaiSanPham.HOA_CHAT;
    }
}
