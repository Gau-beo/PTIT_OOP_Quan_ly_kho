package com.nhom11.quanlykho.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@DiscriminatorValue("CONG_KENH")
@Getter
@Setter
@NoArgsConstructor
public class SanPhamCongKenh extends SanPham {

    @Column(name = "phu_phi_van_chuyen")
    private Double phuPhiVanChuyen;

    public SanPhamCongKenh(String tenSanPham, String donViTinh, Integer soLuongTon,
                            Integer nguongCanhBao, DanhMuc danhMuc, Double phuPhiVanChuyen) {
        super(tenSanPham, donViTinh, soLuongTon, nguongCanhBao, danhMuc);
        this.phuPhiVanChuyen = phuPhiVanChuyen;
    }

    @Override
    public String getThongTinRieng() {
        return "Phụ phí vận chuyển: " + (phuPhiVanChuyen != null ? phuPhiVanChuyen + " đ" : "chưa cập nhật");
    }

    @Override
    public LoaiSanPham getLoaiSanPham() {
        return LoaiSanPham.CONG_KENH;
    }
}
