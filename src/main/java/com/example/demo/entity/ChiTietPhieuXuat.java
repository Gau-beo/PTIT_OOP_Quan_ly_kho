package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "chi_tiet_phieu_xuat")
public class ChiTietPhieuXuat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "phieu_xuat_id")
    private PhieuXuat phieuXuat;

    @ManyToOne
    @JoinColumn(name = "san_pham_id")
    private Product sanPham;

    private int soLuong;

    public ChiTietPhieuXuat() {
    }

    public ChiTietPhieuXuat(PhieuXuat phieuXuat, Product sanPham, int soLuong) {
        this.phieuXuat = phieuXuat;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PhieuXuat getPhieuXuat() { return phieuXuat; }
    public void setPhieuXuat(PhieuXuat phieuXuat) { this.phieuXuat = phieuXuat; }

    public Product getSanPham() { return sanPham; }
    public void setSanPham(Product sanPham) { this.sanPham = sanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
}
