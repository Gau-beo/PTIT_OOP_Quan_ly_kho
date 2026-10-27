package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ChiTietPhieuNhap {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "phieu_nhap_id")
    private PhieuNhap phieuNhap;
    
    @ManyToOne
    @JoinColumn(name = "san_pham_id")
    private Product sanPham;
    
    private int soLuong;
    private double donGiaNhap;

    @Column(name = "han_su_dung")
    private LocalDate hanSuDung;

    public ChiTietPhieuNhap() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PhieuNhap getPhieuNhap() { return phieuNhap; }
    public void setPhieuNhap(PhieuNhap phieuNhap) { this.phieuNhap = phieuNhap; }

    public Product getSanPham() { return sanPham; }
    public void setSanPham(Product sanPham) { this.sanPham = sanPham; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGiaNhap() { return donGiaNhap; }
    public void setDonGiaNhap(double donGiaNhap) { this.donGiaNhap = donGiaNhap; }

    public LocalDate getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(LocalDate hanSuDung) { this.hanSuDung = hanSuDung; }
}
