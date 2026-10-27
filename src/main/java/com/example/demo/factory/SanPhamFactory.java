package com.example.demo.factory;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.demo.entity.*;

/**
 * Factory Pattern: khởi tạo đúng lớp con kế thừa từ Product theo loại sản phẩm,
 * và gán các thuộc tính riêng của từng lớp con từ dữ liệu form.
 */
@Component
public class SanPhamFactory {

    public Product taoSanPham(LoaiSanPham loai, String maSp, String tenSanPham, String donViTinh, Double donGia, String ghiChu) {
        return switch (loai) {
            case THUC_PHAM -> new SanPhamThucPham(maSp, tenSanPham, donViTinh, donGia, ghiChu);
            case DE_VO -> new SanPhamDeVo(maSp, tenSanPham, donViTinh, donGia, ghiChu);
            case HOA_CHAT -> new SanPhamHoaChat(maSp, tenSanPham, donViTinh, donGia, ghiChu);
            case DIEN_TU -> new SanPhamDienTu(maSp, tenSanPham, donViTinh, donGia, ghiChu);
            case CONG_KENH -> new SanPhamCongKenh(maSp, tenSanPham, donViTinh, donGia, ghiChu);
        };
    }

    /**
     * Gán thuộc tính riêng của lớp con từ các tham số form (key = tên trường).
     * Dùng pattern matching instanceof để downcast an toàn.
     */
    public void capNhatThuocTinhRieng(Product product, Map<String, String> p) {
        if (product instanceof SanPhamThucPham tp) {
            tp.setNgayHetHan(docNgay(p.get("ngayHetHan")));
        } else if (product instanceof SanPhamDeVo dv) {
            dv.setMucDoDeVo(docChuoi(p.get("mucDoDeVo")));
            dv.setYeuCauDongGoiDacBiet(p.get("yeuCauDongGoiDacBiet") != null);
        } else if (product instanceof SanPhamHoaChat hc) {
            hc.setMucDoRuiRo(docChuoi(p.get("mucDoRuiRo")));
        } else if (product instanceof SanPhamDienTu dt) {
            dt.setThoiGianBaoHanh(docSoNguyen(p.get("thoiGianBaoHanh")));
            dt.setCongSuat(docSoThuc(p.get("congSuat")));
        } else if (product instanceof SanPhamCongKenh ck) {
            ck.setPhuPhiVanChuyen(docSoThuc(p.get("phuPhiVanChuyen")));
        }
    }

    private static String docChuoi(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private static LocalDate docNgay(String s) {
        return (s == null || s.isBlank()) ? null : LocalDate.parse(s.trim());
    }

    private static Integer docSoNguyen(String s) {
        return (s == null || s.isBlank()) ? null : Integer.valueOf(s.trim());
    }

    private static Double docSoThuc(String s) {
        return (s == null || s.isBlank()) ? null : Double.valueOf(s.trim());
    }
}
