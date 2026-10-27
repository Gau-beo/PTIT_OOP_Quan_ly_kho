package com.example.demo.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.ChiTietPhieuNhapRequest;
import com.example.demo.entity.PhieuNhap;
import com.example.demo.service.NhaCungCapService;
import com.example.demo.service.PhieuNhapService;
import com.example.demo.service.ProductService;

@Controller
public class PhieuNhapController {

    private final PhieuNhapService phieuNhapService;
    private final ProductService productService;
    private final NhaCungCapService nhaCungCapService;

    public PhieuNhapController(PhieuNhapService phieuNhapService,
                               ProductService productService,
                               NhaCungCapService nhaCungCapService) {
        this.phieuNhapService = phieuNhapService;
        this.productService = productService;
        this.nhaCungCapService = nhaCungCapService;
    }

    @GetMapping("/phieunhap")
    public String viewPhieuNhapPage(Model model) {
        model.addAttribute("danhSachPhieuNhap", phieuNhapService.layTatCa());
        model.addAttribute("danhSachSanPham", productService.layTatCaSanPham());
        model.addAttribute("danhSachNCC", nhaCungCapService.layTatCa());
        model.addAttribute("activePage", "phieu-nhap");
        return "phieunhap";
    }

    @PostMapping("/phieunhap/them")
    public String taoPhieuNhap(
            @RequestParam(required = false) Integer nhaCungCapId,   // SỬA: nhận id thay vì tên gõ tay
            @RequestParam(required = false) List<String> sanPhamId,
            @RequestParam(required = false) List<String> soLuong,
            @RequestParam(required = false) List<String> donGiaNhap,
            @RequestParam(required = false) List<String> hanSuDung,
            RedirectAttributes redirectAttributes) {

        try {
            List<ChiTietPhieuNhapRequest> dong = new ArrayList<>();
            if (sanPhamId != null) {
                for (int i = 0; i < sanPhamId.size(); i++) {
                    String sp = sanPhamId.get(i);
                    if (sp == null || sp.isBlank()) {
                        continue;
                    }
                    ChiTietPhieuNhapRequest r = new ChiTietPhieuNhapRequest();
                    r.setSanPhamId(Long.parseLong(sp.trim()));
                    r.setSoLuong(Integer.parseInt(phanTu(soLuong, i, "0")));
                    r.setDonGiaNhap(Double.parseDouble(phanTu(donGiaNhap, i, "0")));
                    String hsd = phanTu(hanSuDung, i, "");
                    if (!hsd.isBlank()) {
                        r.setHanSuDung(LocalDate.parse(hsd));
                    }
                    dong.add(r);
                }
            }
            PhieuNhap pn = phieuNhapService.taoPhieuNhap(nhaCungCapId, dong);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong",
                    "Đã tạo phiếu nhập #" + pn.getId() + " và sinh " + dong.size() + " lô hàng mới!");
        } catch (NumberFormatException | DateTimeParseException e) {
            // SỬA: không hiện lỗi kỹ thuật thô (vd: For input string "abc") cho người dùng
            redirectAttributes.addFlashAttribute("thongBaoLoi",
                    "Không thể tạo phiếu nhập: số lượng, đơn giá hoặc ngày không hợp lệ.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Không thể tạo phiếu nhập: " + e.getMessage());
        }
        return "redirect:/phieunhap";
    }

    private static String phanTu(List<String> ds, int i, String macDinh) {
        if (ds == null || i >= ds.size() || ds.get(i) == null || ds.get(i).isBlank()) {
            return macDinh;
        }
        return ds.get(i).trim();
    }
}
