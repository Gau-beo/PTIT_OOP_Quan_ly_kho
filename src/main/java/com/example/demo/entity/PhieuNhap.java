package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "phieu_nhap")
public class PhieuNhap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngay_nhap")
    private Date ngayNhap;

    @Column(name = "tong_tien")
    private Double tongTien;

    @ManyToOne
    @JoinColumn(name = "nha_cung_cap_id")
    private NhaCungCap nhaCungCap;

    @OneToMany(mappedBy = "phieuNhap", cascade = CascadeType.ALL)
    private List<ChiTietPhieuNhap> chiTietPhieuNhaps;

    public PhieuNhap() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getNgayNhap() { return ngayNhap; }
    public void setNgayNhap(Date ngayNhap) { this.ngayNhap = ngayNhap; }

    public Double getTongTien() { return tongTien; }
    public void setTongTien(Double tongTien) { this.tongTien = tongTien; }

    public NhaCungCap getNhaCungCap() { return nhaCungCap; }
    public void setNhaCungCap(NhaCungCap nhaCungCap) { this.nhaCungCap = nhaCungCap; }

    public List<ChiTietPhieuNhap> getChiTietPhieuNhaps() { return chiTietPhieuNhaps; }
    public void setChiTietPhieuNhaps(List<ChiTietPhieuNhap> chiTietPhieuNhaps) { this.chiTietPhieuNhaps = chiTietPhieuNhaps; }
}
