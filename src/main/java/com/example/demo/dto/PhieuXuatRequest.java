package com.example.demo.dto;

import java.util.List;

public class PhieuXuatRequest {
    private String nguoiNhan;
    private Long nguoiTaoId;
    private List<ChiTietPhieuXuatRequest> chiTietList;

    public String getNguoiNhan() {
        return nguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        this.nguoiNhan = nguoiNhan;
    }

    public Long getNguoiTaoId() {
        return nguoiTaoId;
    }

    public void setNguoiTaoId(Long nguoiTaoId) {
        this.nguoiTaoId = nguoiTaoId;
    }

    public List<ChiTietPhieuXuatRequest> getChiTietList() {
        return chiTietList;
    }

    public void setChiTietList(List<ChiTietPhieuXuatRequest> chiTietList) {
        this.chiTietList = chiTietList;
    }
}