package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PhieuXuat")
public class PhieuXuat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngayXuat")
    private LocalDateTime ngayXuat;

    @Column(name = "nguoiNhan", length = 100)
    private String nguoiNhan;

    // Ánh xạ khóa ngoại tới bảng NguoiDung
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoiTaoId")
    private NguoiDung nguoiTao;

    @OneToMany(mappedBy = "phieuXuat", cascade = CascadeType.ALL)
    private List<ChiTietPhieuXuat> chiTietList = new ArrayList<>();

    public PhieuXuat() {
    }

    public PhieuXuat(LocalDateTime ngayXuat, String nguoiNhan, NguoiDung nguoiTao) {
        this.ngayXuat = ngayXuat;
        this.nguoiNhan = nguoiNhan;
        this.nguoiTao = nguoiTao;
    }

    // PrePersist để tự động gán ngày hiện tại nếu không truyền vào
    @PrePersist
    protected void onCreate() {
        if (this.ngayXuat == null) {
            this.ngayXuat = LocalDateTime.now();
        }
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getNgayXuat() {
        return ngayXuat;
    }

    public void setNgayXuat(LocalDateTime ngayXuat) {
        this.ngayXuat = ngayXuat;
    }

    public String getNguoiNhan() {
        return nguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        this.nguoiNhan = nguoiNhan;
    }

    public NguoiDung getNguoiTao() {
        return nguoiTao;
    }

    public void setNguoiTao(NguoiDung nguoiTao) {
        this.nguoiTao = nguoiTao;
    }

    public List<ChiTietPhieuXuat> getChiTietList() {
        return chiTietList;
    }

    public void setChiTietList(List<ChiTietPhieuXuat> chiTietList) {
        this.chiTietList = chiTietList;
    }
}
