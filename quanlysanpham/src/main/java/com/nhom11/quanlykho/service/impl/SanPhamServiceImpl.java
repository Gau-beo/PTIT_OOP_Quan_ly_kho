package com.nhom11.quanlykho.service.impl;

import com.nhom11.quanlykho.dto.SanPhamRequestDTO;
import com.nhom11.quanlykho.dto.SanPhamResponseDTO;
import com.nhom11.quanlykho.exception.ResourceNotFoundException;
import com.nhom11.quanlykho.factory.SanPhamFactory;
import com.nhom11.quanlykho.model.DanhMuc;
import com.nhom11.quanlykho.model.SanPham;
import com.nhom11.quanlykho.model.SanPhamCongKenh;
import com.nhom11.quanlykho.model.SanPhamDeVo;
import com.nhom11.quanlykho.model.SanPhamDienTu;
import com.nhom11.quanlykho.model.SanPhamHoaChat;
import com.nhom11.quanlykho.model.SanPhamThucPham;
import com.nhom11.quanlykho.repository.DanhMucRepository;
import com.nhom11.quanlykho.repository.SanPhamRepository;
import com.nhom11.quanlykho.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SanPhamServiceImpl implements SanPhamService {

    private final SanPhamRepository sanPhamRepository;
    private final DanhMucRepository danhMucRepository;
    private final SanPhamFactory sanPhamFactory;

    @Override
    public SanPhamResponseDTO taoMoi(SanPhamRequestDTO dto) {
        DanhMuc danhMuc = layDanhMucNeuCo(dto.getDanhMucId());
        SanPham sanPham = sanPhamFactory.taoSanPham(dto, danhMuc);
        return chuyenSangDTO(sanPhamRepository.save(sanPham));
    }

    @Override
    public SanPhamResponseDTO capNhat(Integer id, SanPhamRequestDTO dto) {
        SanPham sanPham = timSanPhamTheoId(id);

        if (sanPham.getLoaiSanPham() != dto.getLoaiSanPham()) {
            throw new IllegalArgumentException("Không thể đổi loại sản phẩm sau khi đã tạo (id=" + id + ")");
        }

        sanPham.setTenSanPham(dto.getTenSanPham());
        sanPham.setDonViTinh(dto.getDonViTinh());
        sanPham.setSoLuongTon(dto.getSoLuongTon());
        sanPham.setNguongCanhBao(dto.getNguongCanhBao());
        sanPham.setDanhMuc(layDanhMucNeuCo(dto.getDanhMucId()));

        // Cập nhật thuộc tính riêng của đúng lớp con (đa hình qua instanceof pattern matching)
        sanPhamFactory.capNhatThuocTinhRieng(sanPham, dto);

        return chuyenSangDTO(sanPhamRepository.save(sanPham));
    }

    @Override
    public void xoa(Integer id) {
        if (!sanPhamRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy sản phẩm có id = " + id);
        }
        sanPhamRepository.deleteById(id);
    }

    @Override
    public SanPhamResponseDTO layTheoId(Integer id) {
        return chuyenSangDTO(timSanPhamTheoId(id));
    }

    @Override
    public List<SanPhamResponseDTO> layTatCa() {
        return sanPhamRepository.findAll().stream().map(this::chuyenSangDTO).toList();
    }

    @Override
    public List<SanPhamResponseDTO> layTheoDanhMuc(Integer danhMucId) {
        return sanPhamRepository.findByDanhMuc_Id(danhMucId).stream().map(this::chuyenSangDTO).toList();
    }

    @Override
    public List<SanPhamResponseDTO> layDanhSachCanhBaoTonKho() {
        return sanPhamRepository.timSanPhamCanCanhBao().stream().map(this::chuyenSangDTO).toList();
    }

    // ----- Hàm hỗ trợ nội bộ -----

    private SanPham timSanPhamTheoId(Integer id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có id = " + id));
    }

    private DanhMuc layDanhMucNeuCo(Integer danhMucId) {
        if (danhMucId == null) return null;
        return danhMucRepository.findById(danhMucId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy danh mục có id = " + danhMucId));
    }

    /**
     * Chuyển entity -> DTO. sanPham.getThongTinRieng() và getLoaiSanPham() là lời gọi
     * đa hình: dù biến khai báo kiểu SanPham, JVM tự chạy đúng phiên bản override
     * của lớp con thực sự (SanPhamThucPham, SanPhamDienTu, ...).
     */
    private SanPhamResponseDTO chuyenSangDTO(SanPham sanPham) {
        SanPhamResponseDTO.SanPhamResponseDTOBuilder builder = SanPhamResponseDTO.builder()
                .id(sanPham.getId())
                .loaiSanPham(sanPham.getLoaiSanPham())
                .tenSanPham(sanPham.getTenSanPham())
                .donViTinh(sanPham.getDonViTinh())
                .soLuongTon(sanPham.getSoLuongTon())
                .nguongCanhBao(sanPham.getNguongCanhBao())
                .danhMucId(sanPham.getDanhMuc() != null ? sanPham.getDanhMuc().getId() : null)
                .tenDanhMuc(sanPham.getDanhMuc() != null ? sanPham.getDanhMuc().getTenDanhMuc() : null)
                .thongTinRieng(sanPham.getThongTinRieng())
                .canCanhBaoTonKho(sanPham.canCanhBaoTonKho());

        // Điền thêm thuộc tính riêng của đúng lớp con để form Sửa có dữ liệu gốc.
        if (sanPham instanceof SanPhamThucPham tp) {
            builder.ngayHetHan(tp.getNgayHetHan());
        } else if (sanPham instanceof SanPhamDeVo dv) {
            builder.mucDoDeVo(dv.getMucDoDeVo()).yeuCauDongGoiDacBiet(dv.getYeuCauDongGoiDacBiet());
        } else if (sanPham instanceof SanPhamHoaChat hc) {
            builder.mucDoRuiRo(hc.getMucDoRuiRo());
        } else if (sanPham instanceof SanPhamDienTu dt) {
            builder.thoiGianBaoHanh(dt.getThoiGianBaoHanh()).congSuat(dt.getCongSuat());
        } else if (sanPham instanceof SanPhamCongKenh ck) {
            builder.phuPhiVanChuyen(ck.getPhuPhiVanChuyen());
        }

        return builder.build();
    }
}
