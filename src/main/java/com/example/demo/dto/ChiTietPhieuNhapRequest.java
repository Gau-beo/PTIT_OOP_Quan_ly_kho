package com.example.demo.dto;

import java.time.LocalDate;

public class ChiTietPhieuNhapRequest {
    private Long sanPhamId;
    private int soLuong;
    private double donGiaNhap;
    private LocalDate hanSuDung;

    public Long getSanPhamId() { return sanPhamId; }
    public void setSanPhamId(Long sanPhamId) { this.sanPhamId = sanPhamId; }

    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }

    public double getDonGiaNhap() { return donGiaNhap; }
    public void setDonGiaNhap(double donGiaNhap) { this.donGiaNhap = donGiaNhap; }

    public LocalDate getHanSuDung() { return hanSuDung; }
    public void setHanSuDung(LocalDate hanSuDung) { this.hanSuDung = hanSuDung; }
}
