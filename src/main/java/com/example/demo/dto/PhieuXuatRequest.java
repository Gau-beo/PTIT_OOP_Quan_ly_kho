package com.example.demo.dto;

import java.util.List;

public class PhieuXuatRequest {
    private String nguoiNhan;
    private String nguoiTaoEmail;
    private List<ChiTietPhieuXuatRequest> chiTietList;

    public String getNguoiNhan() {
        return nguoiNhan;
    }

    public void setNguoiNhan(String nguoiNhan) {
        this.nguoiNhan = nguoiNhan;
    }

    public String getNguoiTaoEmail() {
        return nguoiTaoEmail;
    }

    public void setNguoiTaoEmail(String nguoiTaoEmail) {
        this.nguoiTaoEmail = nguoiTaoEmail;
    }

    public List<ChiTietPhieuXuatRequest> getChiTietList() {
        return chiTietList;
    }

    public void setChiTietList(List<ChiTietPhieuXuatRequest> chiTietList) {
        this.chiTietList = chiTietList;
    }
}