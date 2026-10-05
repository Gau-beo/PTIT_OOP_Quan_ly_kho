package com.nhom11.quanlykho.service;

import com.nhom11.quanlykho.model.DanhMuc;

import java.util.List;

public interface DanhMucService {
    DanhMuc taoMoi(String tenDanhMuc);
    DanhMuc capNhat(Integer id, String tenDanhMuc);
    void xoa(Integer id);
    DanhMuc layTheoId(Integer id);
    List<DanhMuc> layTatCa();
}
