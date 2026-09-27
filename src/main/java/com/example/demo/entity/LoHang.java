package com.example.demo.entity;

import java.time.LocalDate;

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

    private String maLoHang;
    private LocalDate ngayNhap;
    private int soLuong;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaLoHang() { return maLoHang; }
    public void setMaLoHang(String maLoHang) { this.maLoHang = maLoHang; }

    public LocalDate getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(LocalDate ngayNhap) { this.ngayNhap = ngayNhap; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
