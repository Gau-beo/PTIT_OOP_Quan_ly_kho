package com.nhom11.quanlykho.service.impl;

import com.nhom11.quanlykho.exception.ResourceNotFoundException;
import com.nhom11.quanlykho.model.DanhMuc;
import com.nhom11.quanlykho.repository.DanhMucRepository;
import com.nhom11.quanlykho.service.DanhMucService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DanhMucServiceImpl implements DanhMucService {

    private final DanhMucRepository danhMucRepository;

    @Override
    public DanhMuc taoMoi(String tenDanhMuc) {
        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setTenDanhMuc(tenDanhMuc);
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public DanhMuc capNhat(Integer id, String tenDanhMuc) {
        DanhMuc danhMuc = layTheoId(id);
        danhMuc.setTenDanhMuc(tenDanhMuc);
        return danhMucRepository.save(danhMuc);
    }

    @Override
    public void xoa(Integer id) {
        if (!danhMucRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy danh mục có id = " + id);
        }
        danhMucRepository.deleteById(id);
    }

    @Override
    public DanhMuc layTheoId(Integer id) {
        return danhMucRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục có id = " + id));
    }

    @Override
    public List<DanhMuc> layTatCa() {
        return danhMucRepository.findAll();
    }
}
