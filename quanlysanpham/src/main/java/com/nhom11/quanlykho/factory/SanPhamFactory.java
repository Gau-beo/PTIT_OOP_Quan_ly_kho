package com.nhom11.quanlykho.factory;

import com.nhom11.quanlykho.dto.SanPhamRequestDTO;
import com.nhom11.quanlykho.model.*;
import org.springframework.stereotype.Component;

/**
 * Factory Pattern: nơi duy nhất "biết" cách khởi tạo từng loại sản phẩm con.
 * Service chỉ cần gọi SanPhamFactory.taoSanPham(dto, danhMuc), không cần biết
 * chi tiết constructor của từng lớp con -> giảm phụ thuộc, dễ mở rộng thêm loại mới.
 */
@Component
public class SanPhamFactory {

    public SanPham taoSanPham(SanPhamRequestDTO dto, DanhMuc danhMuc) {
        return switch (dto.getLoaiSanPham()) {
            case THUC_PHAM -> new SanPhamThucPham(
                    dto.getTenSanPham(), dto.getDonViTinh(), dto.getSoLuongTon(),
                    dto.getNguongCanhBao(), danhMuc, dto.getNgayHetHan());

            case DE_VO -> new SanPhamDeVo(
                    dto.getTenSanPham(), dto.getDonViTinh(), dto.getSoLuongTon(),
                    dto.getNguongCanhBao(), danhMuc, dto.getMucDoDeVo(), dto.getYeuCauDongGoiDacBiet());

            case HOA_CHAT -> new SanPhamHoaChat(
                    dto.getTenSanPham(), dto.getDonViTinh(), dto.getSoLuongTon(),
                    dto.getNguongCanhBao(), danhMuc, dto.getMucDoRuiRo());

            case DIEN_TU -> new SanPhamDienTu(
                    dto.getTenSanPham(), dto.getDonViTinh(), dto.getSoLuongTon(),
                    dto.getNguongCanhBao(), danhMuc, dto.getThoiGianBaoHanh(), dto.getCongSuat());

            case CONG_KENH -> new SanPhamCongKenh(
                    dto.getTenSanPham(), dto.getDonViTinh(), dto.getSoLuongTon(),
                    dto.getNguongCanhBao(), danhMuc, dto.getPhuPhiVanChuyen());
        };
    }

    /**
     * Cập nhật các thuộc tính riêng của lớp con dựa trên loại thực tế của đối tượng.
     * Dùng instanceof pattern matching (Java 17+) để "downcast" an toàn.
     */
    public void capNhatThuocTinhRieng(SanPham sanPham, SanPhamRequestDTO dto) {
        if (sanPham instanceof SanPhamThucPham tp) {
            tp.setNgayHetHan(dto.getNgayHetHan());
        } else if (sanPham instanceof SanPhamDeVo dv) {
            dv.setMucDoDeVo(dto.getMucDoDeVo());
            dv.setYeuCauDongGoiDacBiet(dto.getYeuCauDongGoiDacBiet());
        } else if (sanPham instanceof SanPhamHoaChat hc) {
            hc.setMucDoRuiRo(dto.getMucDoRuiRo());
        } else if (sanPham instanceof SanPhamDienTu dt) {
            dt.setThoiGianBaoHanh(dto.getThoiGianBaoHanh());
            dt.setCongSuat(dto.getCongSuat());
        } else if (sanPham instanceof SanPhamCongKenh ck) {
            ck.setPhuPhiVanChuyen(dto.getPhuPhiVanChuyen());
        }
    }
}
