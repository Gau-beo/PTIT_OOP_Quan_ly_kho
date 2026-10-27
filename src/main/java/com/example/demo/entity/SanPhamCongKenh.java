package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CONG_KENH")
public class SanPhamCongKenh extends Product {

    @Column(name = "phu_phi_van_chuyen")
    private Double phuPhiVanChuyen;

    public SanPhamCongKenh() {
        // Hàm tạo rỗng thay thế cho @NoArgsConstructor
    }

    public SanPhamCongKenh(String maSp, String tenSanPham, String donViTinh, Double donGia, String ghiChu) {
        super(maSp, tenSanPham, donViTinh, donGia, ghiChu);
    }

    public Double getPhuPhiVanChuyen() {
        return phuPhiVanChuyen;
    }

    public void setPhuPhiVanChuyen(Double phuPhiVanChuyen) {
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