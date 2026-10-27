package com.example.demo.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "loai_san_pham")
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String maSanPham;

    @Column(nullable = false)
    private String tenSanPham;

    private String donViTinh;

    private Double donGia;

    @Column(columnDefinition = "TEXT")
    private String ghiChu;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("product")
    private List<LoHang> loHangs = new ArrayList<>();

    public Product() {
    }

    public Product(String maSanPham, String tenSanPham, String donViTinh, Double donGia, String ghiChu) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.donViTinh = donViTinh;
        this.donGia = donGia;
        this.ghiChu = ghiChu;
    }


    // ===== Đa hình (Polymorphism): mỗi lớp con tự định nghĩa =====
    public abstract String getThongTinRieng();

    public abstract LoaiSanPham getLoaiSanPham();

    public int getTongTonKho() {
        if (loHangs == null || loHangs.isEmpty()) {
            return 0;
        }
        return loHangs.stream().mapToInt(LoHang::getSoLuong).sum();
    }

    /** Tồn kho có thể xuất: không tính các lô đã hết hạn. */
    public int getTonKhoKhaDung() {
        if (loHangs == null) {
            return 0;
        }
        return loHangs.stream().filter(lh -> !lh.isHetHan()).mapToInt(LoHang::getSoLuong).sum();
    }

    public int getSoLuongLoHang() {
        return loHangs != null ? loHangs.size() : 0;
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getMaSanPham() { return maSanPham; }
    public void setMaSanPham(String maSanPham) { this.maSanPham = maSanPham; }

    public String getTenSanPham() { return tenSanPham; }
    public void setTenSanPham(String tenSanPham) { this.tenSanPham = tenSanPham; }
    
    public String getDonViTinh() { return donViTinh; }
    public void setDonViTinh(String donViTinh) { this.donViTinh = donViTinh; }

    public Double getDonGia() { return donGia; }
    public void setDonGia(Double donGia) { this.donGia = donGia; }

    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }

    public List<LoHang> getLoHangs() { return loHangs; }
    public void setLoHangs(List<LoHang> loHangs) { this.loHangs = loHangs; }
}
