package com.example.demo.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.NhaCungCap;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.NhaCungCapRepository;
import com.example.demo.repository.PhieuNhapRepository;

@Service
public class NhaCungCapService {

    private final NhaCungCapRepository nhaCungCapRepository;
    private final PhieuNhapRepository phieuNhapRepository;

    public NhaCungCapService(NhaCungCapRepository nhaCungCapRepository,
                             PhieuNhapRepository phieuNhapRepository) {
        this.nhaCungCapRepository = nhaCungCapRepository;
        this.phieuNhapRepository = phieuNhapRepository;
    }

    @Transactional(readOnly = true)
    public List<NhaCungCap> layTatCa() {
        return nhaCungCapRepository.findAll(Sort.by(Sort.Direction.ASC, "tenNCC"));
    }

    @Transactional(readOnly = true)
    public NhaCungCap layTheoId(Integer id) {
        return nhaCungCapRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhà cung cấp ID: " + id));
    }

    @Transactional
    public NhaCungCap them(NhaCungCap ncc) {
        chuanHoaVaKiemTra(ncc);
        if (nhaCungCapRepository.existsByTenNCCIgnoreCase(ncc.getTenNCC())) {
            throw new IllegalArgumentException("Nhà cung cấp '" + ncc.getTenNCC() + "' đã tồn tại!");
        }
        ncc.setId(null);
        return nhaCungCapRepository.save(ncc);
    }

    @Transactional
    public NhaCungCap capNhat(Integer id, NhaCungCap duLieuMoi) {
        NhaCungCap ncc = layTheoId(id);
        chuanHoaVaKiemTra(duLieuMoi);
        if (nhaCungCapRepository.existsByTenNCCIgnoreCaseAndIdNot(duLieuMoi.getTenNCC(), id)) {
            throw new IllegalArgumentException("Đã có nhà cung cấp khác tên '" + duLieuMoi.getTenNCC() + "'!");
        }
        ncc.setTenNCC(duLieuMoi.getTenNCC());
        ncc.setDiaChi(duLieuMoi.getDiaChi());
        ncc.setSdt(duLieuMoi.getSdt());
        ncc.setEmail(duLieuMoi.getEmail());
        return nhaCungCapRepository.save(ncc);
    }

    @Transactional
    public void xoa(Integer id) {
        NhaCungCap ncc = layTheoId(id);
        if (phieuNhapRepository.existsByNhaCungCap_Id(id)) {
            throw new IllegalArgumentException("Không thể xóa '" + ncc.getTenNCC()
                    + "' vì đã có phiếu nhập từ nhà cung cấp này!");
        }
        nhaCungCapRepository.delete(ncc);
    }

    private void chuanHoaVaKiemTra(NhaCungCap ncc) {
        ncc.setTenNCC(ncc.getTenNCC() == null ? "" : ncc.getTenNCC().trim());
        ncc.setDiaChi(rongThanhNull(ncc.getDiaChi()));
        ncc.setSdt(rongThanhNull(ncc.getSdt()));
        ncc.setEmail(rongThanhNull(ncc.getEmail()));

        if (ncc.getTenNCC().isEmpty()) {
            throw new IllegalArgumentException("Tên nhà cung cấp không được để trống!");
        }
        if (ncc.getSdt() != null && !ncc.getSdt().matches("[0-9+\\-.() ]{8,20}")) {
            throw new IllegalArgumentException("Số điện thoại không hợp lệ (8–20 ký tự, chỉ gồm số và + - . ( ))!");
        }
        if (ncc.getEmail() != null && !ncc.getEmail().matches("[^@\\s]+@[^@\\s]+\\.[^@\\s]+")) {
            throw new IllegalArgumentException("Email không hợp lệ!");
        }
    }

    private static String rongThanhNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
