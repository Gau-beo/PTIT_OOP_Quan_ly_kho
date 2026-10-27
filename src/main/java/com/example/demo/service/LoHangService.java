package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.LoHang;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.LoHangRepository;

@Service
@Transactional
public class LoHangService {

    /** Số ngày trước hạn được coi là "sắp hết hạn" (khớp với LoHang.isSapHetHan()). */
    private static final int SO_NGAY_SAP_HET_HAN = 30;

    private final LoHangRepository loHangRepository;

    public LoHangService(LoHangRepository loHangRepository) {
        this.loHangRepository = loHangRepository;
    }

    @Transactional(readOnly = true)
    public LoHang layLoHangTheoId(Long id) {
        return loHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy lô hàng có ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<LoHang> traCuuNangCao(String keyword, String trangThai) {
        List<LoHang> danhSach = loHangRepository.traCuu(keyword != null ? keyword.trim() : "");

        if (trangThai == null || trangThai.isBlank() || "tat_ca".equalsIgnoreCase(trangThai)) {
            return danhSach;
        }

        return danhSach.stream().filter(lh -> {
            if ("con_han".equalsIgnoreCase(trangThai)) {
                return !lh.isHetHan() && !lh.isSapHetHan();
            } else if ("sap_het_han".equalsIgnoreCase(trangThai)) {
                return lh.isSapHetHan();
            } else if ("het_han".equalsIgnoreCase(trangThai)) {
                return lh.isHetHan();
            } else if ("het_hang".equalsIgnoreCase(trangThai)) {
                return lh.getSoLuong() == 0;
            }
            return true;
        }).collect(Collectors.toList());
    }

    /**
     * Khai báo một lô hàng trực tiếp (dùng cho tồn đầu kỳ / hàng có sẵn trong kho).
     * Hàng nhập thông thường nên đi qua Phiếu nhập để có chứng từ.
     */
    public LoHang themLoHang(LoHang loHang) {
        String ma = loHang.getMaLoHang() == null ? "" : loHang.getMaLoHang().trim();
        if (ma.isEmpty()) {
            throw new IllegalArgumentException("Mã lô hàng không được để trống!");
        }
        if (loHangRepository.existsByMaLoHang(ma)) {
            throw new IllegalArgumentException("Mã lô hàng '" + ma + "' đã tồn tại!");
        }
        if (loHang.getProduct() == null) {
            throw new IllegalArgumentException("Vui lòng chọn sản phẩm cho lô hàng!");
        }
        if (loHang.getSoLuong() <= 0) {
            throw new IllegalArgumentException("Số lượng lô hàng phải lớn hơn 0!");
        }
        if (loHang.getNgayNhap() == null) {
            throw new IllegalArgumentException("Ngày nhập không được để trống!");
        }
        if (loHang.getHanSuDung() != null && loHang.getHanSuDung().isBefore(loHang.getNgayNhap())) {
            throw new IllegalArgumentException("Hạn sử dụng không được trước ngày nhập!");
        }
        loHang.setMaLoHang(ma);
        return loHangRepository.save(loHang);
    }

    /**
     * Tiêu hủy lô đã hết hạn: đưa số lượng còn lại về 0 (lô vẫn được giữ lại để truy vết).
     * @return số lượng đã tiêu hủy
     */
    public int tieuHuyLoHang(Long id) {
        LoHang lo = layLoHangTheoId(id);
        if (!lo.isHetHan()) {
            throw new IllegalArgumentException("Chỉ được tiêu hủy lô đã hết hạn sử dụng!");
        }
        int conLai = lo.getSoLuong();
        if (conLai <= 0) {
            throw new IllegalArgumentException("Lô '" + lo.getMaLoHang() + "' đã hết hàng, không còn gì để tiêu hủy!");
        }
        lo.setSoLuong(0);
        loHangRepository.save(lo);
        return conLai;
    }

    @Transactional(readOnly = true)
    public long layTongSoLoHang() {
        return loHangRepository.count();
    }

    /** Các lô đã hết hạn mà vẫn còn hàng trong kho. */
    @Transactional(readOnly = true)
    public List<LoHang> layLoHangHetHan() {
        return loHangRepository.findByHanSuDungBeforeAndSoLuongGreaterThan(LocalDate.now(), 0);
    }

    /** Các lô sắp hết hạn (trong vòng 30 ngày tới) và còn hàng. */
    @Transactional(readOnly = true)
    public List<LoHang> layLoHangSapHetHan() {
        LocalDate homNay = LocalDate.now();
        return loHangRepository.findByHanSuDungBetweenAndSoLuongGreaterThan(
                homNay, homNay.plusDays(SO_NGAY_SAP_HET_HAN - 1), 0);
    }
}
