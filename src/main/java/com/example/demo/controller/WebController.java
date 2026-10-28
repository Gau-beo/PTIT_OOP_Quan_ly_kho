package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.LoHang;
import com.example.demo.entity.LoaiSanPham;
import com.example.demo.entity.Product;
import com.example.demo.factory.SanPhamFactory;
import com.example.demo.service.LoHangService;
import com.example.demo.service.NhaCungCapService;
import com.example.demo.service.ProductService;

@Controller
public class WebController {

    private final ProductService productService;
    private final LoHangService loHangService;
    private final NhaCungCapService nhaCungCapService;
    private final SanPhamFactory sanPhamFactory;

    public WebController(ProductService productService,
                         LoHangService loHangService,
                         NhaCungCapService nhaCungCapService,
                         SanPhamFactory sanPhamFactory) {
        this.productService = productService;
        this.loHangService = loHangService;
        this.nhaCungCapService = nhaCungCapService;
        this.sanPhamFactory = sanPhamFactory;
    }

    @GetMapping({"/", "/thong-ke"})
    public String trangThongKe(Model model) {
        List<LoHang> loHangSapHetHan = loHangService.layLoHangSapHetHan();
        List<LoHang> loHangHetHan = loHangService.layLoHangHetHan();

        model.addAttribute("tongSoSanPham", productService.layTongSoSanPham());
        model.addAttribute("tongSoLoHang", loHangService.layTongSoLoHang());
        model.addAttribute("tongTonKho", productService.layTongTonKho());
        model.addAttribute("tongTonKhaDung", productService.layTongTonKhoKhaDung());
        model.addAttribute("soLuongSapHetHan", loHangSapHetHan.size());
        model.addAttribute("soLuongHetHan", loHangHetHan.size());
        model.addAttribute("loHangSapHetHan", loHangSapHetHan);
        model.addAttribute("loHangHetHan", loHangHetHan);
        model.addAttribute("danhSachSanPham", productService.layTatCaSanPham());
        model.addAttribute("activePage", "thong-ke");

        return "thong-ke";
    }

    @GetMapping("/lo-hang")
    public String trangTraCuuLoHang(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "tat_ca") String trangThai,
            Model model) {

        model.addAttribute("danhSachLoHang", loHangService.traCuuNangCao(keyword, trangThai));
        model.addAttribute("danhSachSanPham", productService.layTatCaSanPham());
        model.addAttribute("danhSachNCC", nhaCungCapService.layTatCa());
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("activePage", "lo-hang");

        return "lo-hang";
    }

    @PostMapping("/lo-hang/them")
    public String themLoHang(
            @RequestParam String maLoHang,
            @RequestParam Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayNhap,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hanSuDung,
            @RequestParam int soLuong,
            @RequestParam(required = false) String nhaCungCap,
            RedirectAttributes redirectAttributes) {

        try {
            Product product = productService.laySanPhamTheoId(productId);
            String ncc = (nhaCungCap == null || nhaCungCap.isBlank()) ? null : nhaCungCap.trim();
            LoHang loHang = new LoHang(maLoHang, ngayNhap, hanSuDung, soLuong, ncc, product);
            loHangService.themLoHang(loHang);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Thêm lô hàng [" + maLoHang.trim() + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi thêm lô hàng: " + e.getMessage());
        }

        return "redirect:/lo-hang";
    }

    @PostMapping("/lo-hang/{id}/tieu-huy")
    public String tieuHuyLoHang(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            int soLuong = loHangService.tieuHuyLoHang(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong",
                    "Đã tiêu hủy " + soLuong + " đơn vị còn lại của lô hết hạn.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Không thể tiêu hủy lô: " + e.getMessage());
        }
        return "redirect:/lo-hang";
    }

    @GetMapping("/san-pham")
    public String trangSanPham(Model model) {
        model.addAttribute("danhSachSanPham", productService.layTatCaSanPham());
        model.addAttribute("loaiSanPhams", LoaiSanPham.values());
        model.addAttribute("activePage", "san-pham");
        return "san-pham";
    }

    @PostMapping("/san-pham/them")
    public String themSanPham(
            @RequestParam String maSanPham,
            @RequestParam String tenSanPham,
            @RequestParam(required = false) String donViTinh,
            @RequestParam(required = false, defaultValue = "0") Double donGia,
            @RequestParam(required = false) String ghiChu,
            @RequestParam(defaultValue = "THUC_PHAM") LoaiSanPham loai,
            @RequestParam Map<String, String> thuocTinhRieng,
            RedirectAttributes redirectAttributes) {

        try {
            String ma = maSanPham.trim();
            String ten = tenSanPham.trim();
            if (ma.isEmpty()) {
                throw new IllegalArgumentException("Mã sản phẩm không được để trống!");
            }
            if (ten.isEmpty()) {
                throw new IllegalArgumentException("Tên sản phẩm không được để trống!");
            }
            if (donGia == null || donGia < 0) {
                throw new IllegalArgumentException("Đơn giá không được âm!");
            }
            if (productService.tonTaiMaSanPham(ma)) {
                throw new IllegalArgumentException("Mã sản phẩm [" + ma + "] đã tồn tại!");
            }
            String dvt = (donViTinh == null || donViTinh.isBlank()) ? "Cái" : donViTinh.trim();
            Product product = sanPhamFactory.taoSanPham(loai, ma, ten, dvt, donGia, ghiChu == null ? "" : ghiChu.trim());
            sanPhamFactory.capNhatThuocTinhRieng(product, thuocTinhRieng);
            productService.luuSanPham(product);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong",
                    "Thêm sản phẩm [" + ten + "] (" + loai.getTenHienThi() + ") thành công!");
        } catch (NumberFormatException | DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi thêm sản phẩm: giá trị số không hợp lệ.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }

        return "redirect:/san-pham";
    }

    @PostMapping("/san-pham/{id}/sua")
    public String suaSanPham(
            @PathVariable Long id,
            @RequestParam String tenSanPham,
            @RequestParam(required = false) String donViTinh,
            @RequestParam(required = false, defaultValue = "0") Double donGia,
            @RequestParam(required = false) String ghiChu,
            @RequestParam Map<String, String> thuocTinhRieng,
            RedirectAttributes redirectAttributes) {

        try {
            Product product = productService.capNhatSanPham(id, tenSanPham, donViTinh, donGia, ghiChu, thuocTinhRieng);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong",
                    "Đã cập nhật sản phẩm: " + product.getTenSanPham());
        } catch (NumberFormatException | DateTimeParseException e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi cập nhật sản phẩm: giá trị số không hợp lệ.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }

        return "redirect:/san-pham";
    }

    @PostMapping("/san-pham/{id}/xoa")
    public String xoaSanPham(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.xoaSanPham(id);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã xóa sản phẩm ID " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", e.getMessage());
        }
        return "redirect:/san-pham";
    }
}
