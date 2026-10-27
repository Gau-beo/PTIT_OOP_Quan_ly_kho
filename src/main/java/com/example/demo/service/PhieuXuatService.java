package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ChiTietPhieuXuatRequest;
import com.example.demo.dto.PhieuXuatRequest;
import com.example.demo.entity.ChiTietPhieuXuat;
import com.example.demo.entity.LoHang;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.PhieuXuat;
import com.example.demo.entity.Product;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.PhieuXuatRepository;
import com.example.demo.repository.ProductRepository;

@Service
@Transactional
public class PhieuXuatService {

    /** FEFO: lô hết hạn sớm nhất xuất trước; cùng hạn thì lô nhập trước xuất trước. */
    private static final Comparator<LoHang> UU_TIEN_FEFO = Comparator
            .comparing(LoHang::getHanSuDung, Comparator.nullsLast(Comparator.<LocalDate>naturalOrder()))
            .thenComparing(LoHang::getNgayNhap, Comparator.nullsLast(Comparator.<LocalDate>naturalOrder()));

    private final PhieuXuatRepository phieuXuatRepository;
    private final ProductRepository productRepository;
    private final NguoiDungRepository nguoiDungRepository;

    public PhieuXuatService(PhieuXuatRepository phieuXuatRepository,
                            ProductRepository productRepository,
                            NguoiDungRepository nguoiDungRepository) {
        this.phieuXuatRepository = phieuXuatRepository;
        this.productRepository = productRepository;
        this.nguoiDungRepository = nguoiDungRepository;
    }

    @Transactional(readOnly = true)
    public List<PhieuXuat> layTatCa() {
        return phieuXuatRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public PhieuXuat taoPhieuXuat(PhieuXuatRequest request) {
        if (request.getNguoiNhan() == null || request.getNguoiNhan().isBlank()) {
            throw new IllegalArgumentException("Vui lòng nhập tên người nhận hàng!");
        }
        if (request.getChiTietList() == null || request.getChiTietList().isEmpty()) {
            throw new IllegalArgumentException("Phiếu xuất phải có ít nhất một dòng sản phẩm!");
        }

        NguoiDung nguoiTao = nguoiDungRepository.findById(request.getNguoiTaoId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng ID: " + request.getNguoiTaoId()));

        PhieuXuat phieuXuat = new PhieuXuat(LocalDateTime.now(), request.getNguoiNhan().trim(), nguoiTao);

        for (ChiTietPhieuXuatRequest item : request.getChiTietList()) {
            int canXuat = item.getSoLuong() == null ? 0 : item.getSoLuong();
            if (canXuat <= 0) {
                throw new IllegalArgumentException("Số lượng xuất phải lớn hơn 0!");
            }
            Product product = productRepository.findById(item.getSanPhamId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm ID: " + item.getSanPhamId()));

            // Chỉ xuất từ các lô còn hàng và chưa hết hạn, ưu tiên theo FEFO
            List<LoHang> loKhaDung = product.getLoHangs().stream()
                    .filter(lh -> lh.getSoLuong() > 0 && !lh.isHetHan())
                    .sorted(UU_TIEN_FEFO)
                    .toList();

            int tongKhaDung = loKhaDung.stream().mapToInt(LoHang::getSoLuong).sum();
            if (tongKhaDung < canXuat) {
                throw new IllegalArgumentException("Sản phẩm '" + product.getTenSanPham() + "' chỉ còn "
                        + tongKhaDung + " khả dụng (không tính lô hết hạn), không đủ để xuất " + canXuat + "!");
            }

            int conLai = canXuat;
            for (LoHang lo : loKhaDung) {
                if (conLai <= 0) {
                    break;
                }
                int lay = Math.min(lo.getSoLuong(), conLai);
                lo.setSoLuong(lo.getSoLuong() - lay);
                conLai -= lay;
            }

            phieuXuat.getChiTietList().add(new ChiTietPhieuXuat(phieuXuat, product, canXuat));
        }

        return phieuXuatRepository.save(phieuXuat);
    }
}
