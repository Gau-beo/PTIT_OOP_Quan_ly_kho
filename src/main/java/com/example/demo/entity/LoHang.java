package com.example.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lo_hang")
public class LoHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String maLoHang;

    @Column(nullable = false)
    private LocalDate ngayNhap;

    private LocalDate hanSuDung;

    @Column(nullable = false)
    private int soLuong;

    private String nhaCungCap;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public LoHang() {
    }

    public LoHang(String maLoHang, LocalDate ngayNhap, LocalDate hanSuDung, int soLuong, String nhaCungCap, Product product) {
        this.maLoHang = maLoHang;
        this.ngayNhap = ngayNhap;
        this.hanSuDung = hanSuDung;
        this.soLuong = soLuong;
        this.nhaCungCap = nhaCungCap;
        this.product = product;
    }

    public boolean isHetHan() {
        return hanSuDung != null && hanSuDung.isBefore(LocalDate.now());
    }

    public boolean isSapHetHan() {
        if (hanSuDung == null || isHetHan()) {
            return false;
        }
        return hanSuDung.isBefore(LocalDate.now().plusDays(30));
    }

    public String getTrangThai() {
        if (hanSuDung == null) {
            return "Không có HSD";
        }
        if (isHetHan()) {
            return "Đã hết hạn";
        }
        if (isSapHetHan()) {
            return "Sắp hết hạn";
        }
        return "Còn hạn";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaLoHang() { return maLoHang; }
    public void setMaLoHang(String maLoHang) { this.maLoHang = maLoHang; }

    public LocalDate getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDate ngayNhap) { this.ngayNhap = ngayNhap; }

    public LocalDate getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(LocalDate hanSuDung) { this.hanSuDung = hanSuDung; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public String getNhaCungCap() { return nhaCungCap; }
    public void setNhaCungCap(String nhaCungCap) { this.nhaCungCap = nhaCungCap; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}

