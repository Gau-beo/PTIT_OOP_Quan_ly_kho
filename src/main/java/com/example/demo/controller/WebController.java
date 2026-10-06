package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.LoHang;
import com.example.demo.entity.Product;
import com.example.demo.service.LoHangService;
import com.example.demo.service.ProductService;

@Controller
public class WebController {

    private final ProductService productService;
    private final LoHangService loHangService;

    public WebController(ProductService productService, LoHangService loHangService) {
        this.productService = productService;
        this.loHangService = loHangService;
    }

    // 1. TRANG BÁO CÁO & THỐNG KÊ (Trang chủ)
    @GetMapping({"/", "/thong-ke"})
    public String trangThongKe(Model model) {
        long tongSoSanPham = productService.layTongSoSanPham();
        long tongSoLoHang = loHangService.layTongSoLoHang();
        long tongTonKho = productService.layTongTonKho();
        List<LoHang> loHangSapHetHan = loHangService.layLoHangSapHetHan();
        List<LoHang> loHangHetHan = loHangService.layLoHangHetHan();
        List<Product> danhSachSanPham = productService.layTatCaSanPham();

        model.addAttribute("tongSoSanPham", tongSoSanPham);
        model.addAttribute("tongSoLoHang", tongSoLoHang);
        model.addAttribute("tongTonKho", tongTonKho);
        model.addAttribute("soLuongSapHetHan", loHangSapHetHan.size());
        model.addAttribute("soLuongHetHan", loHangHetHan.size());
        model.addAttribute("loHangSapHetHan", loHangSapHetHan);
        model.addAttribute("loHangHetHan", loHangHetHan);
        model.addAttribute("danhSachSanPham", danhSachSanPham);
        model.addAttribute("activePage", "thong-ke");

        return "thong-ke";
    }

    // 2. TRANG TRA CỨU LÔ HÀNG
    @GetMapping("/lo-hang")
    public String trangTraCuuLoHang(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false, defaultValue = "tat_ca") String trangThai,
            Model model) {

        List<LoHang> danhSachLoHang = loHangService.traCuuNangCao(keyword, trangThai);
        List<Product> danhSachSanPham = productService.layTatCaSanPham();

        model.addAttribute("danhSachLoHang", danhSachLoHang);
        model.addAttribute("danhSachSanPham", danhSachSanPham);
        model.addAttribute("keyword", keyword);
        model.addAttribute("trangThai", trangThai);
        model.addAttribute("activePage", "lo-hang");

        return "lo-hang";
    }

    // Thêm lô hàng mới từ giao diện Web
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
            LoHang loHang = new LoHang(maLoHang, ngayNhap, hanSuDung, soLuong, nhaCungCap, product);
            loHangService.themLoHang(loHang);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Thêm lô hàng [" + maLoHang + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi thêm lô hàng: " + e.getMessage());
        }

        return "redirect:/lo-hang";
    }

    // 3. TRANG GHI CHÚ SẢN PHẨM & QUẢN LÝ SẢN PHẨM
    @GetMapping("/san-pham")
    public String trangSanPham(Model model) {
        List<Product> danhSachSanPham = productService.layTatCaSanPham();
        model.addAttribute("danhSachSanPham", danhSachSanPham);
        model.addAttribute("activePage", "san-pham");
        return "san-pham";
    }

    // Cập nhật ghi chú sản phẩm từ giao diện Web
    @PostMapping("/san-pham/{id}/ghi-chu")
    public String capNhatGhiChuWeb(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "") String ghiChu,
            RedirectAttributes redirectAttributes) {

        try {
            Product product = productService.capNhatGhiChuText(id, ghiChu);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã cập nhật ghi chú cho sản phẩm: " + product.getTenSanPham());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi cập nhật ghi chú: " + e.getMessage());
        }

        return "redirect:/san-pham";
    }

    // Thêm sản phẩm mới từ giao diện Web
    @PostMapping("/san-pham/them")
    public String themSanPham(
            @RequestParam String maSanPham,
            @RequestParam String tenSanPham,
            @RequestParam(required = false) String donViTinh,
            @RequestParam(required = false, defaultValue = "0") Double donGia,
            @RequestParam(required = false) String ghiChu,
            RedirectAttributes redirectAttributes) {

        try {
            Product product = new Product(maSanPham, tenSanPham, donViTinh, donGia, ghiChu);
            productService.luuSanPham(product);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Thêm sản phẩm [" + tenSanPham + "] thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }

        return "redirect:/san-pham";
    }
}
