package com.nhom11.quanlykho.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("THUC_PHAM")
@Getter
@Setter
@NoArgsConstructor
public class SanPhamThucPham extends SanPham {

    @Column(name = "ngay_het_han")
    private LocalDate ngayHetHan;

    public SanPhamThucPham(String tenSanPham, String donViTinh, Integer soLuongTon,
                            Integer nguongCanhBao, DanhMuc danhMuc, LocalDate ngayHetHan) {
        super(tenSanPham, donViTinh, soLuongTon, nguongCanhBao, danhMuc);
        this.ngayHetHan = ngayHetHan;
    }

    public boolean daHetHan() {
        return ngayHetHan != null && ngayHetHan.isBefore(LocalDate.now());
    }

    @Override
    public String getThongTinRieng() {
        return "Hạn sử dụng: " + (ngayHetHan != null ? ngayHetHan : "chưa cập nhật")
                + (daHetHan() ? " (ĐÃ HẾT HẠN)" : "");
    }

    @Override
    public LoaiSanPham getLoaiSanPham() {
        return LoaiSanPham.THUC_PHAM;
    }
}
