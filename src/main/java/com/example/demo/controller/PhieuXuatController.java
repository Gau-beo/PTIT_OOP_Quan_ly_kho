package com.example.demo.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dto.ChiTietPhieuXuatRequest;
import com.example.demo.dto.PhieuXuatRequest;
import com.example.demo.entity.PhieuXuat;
import com.example.demo.service.PhieuXuatService;
import com.example.demo.service.ProductService;

@Controller
public class PhieuXuatController {

    private final PhieuXuatService phieuXuatService;
    private final ProductService productService;

    public PhieuXuatController(PhieuXuatService phieuXuatService, ProductService productService) {
        this.phieuXuatService = phieuXuatService;
        this.productService = productService;
    }

    @GetMapping("/phieuxuat")
    public String trangPhieuXuat(Model model) {
        model.addAttribute("danhSachPhieuXuat", phieuXuatService.layTatCa());
        model.addAttribute("danhSachSanPham", productService.layTatCaSanPham());
        model.addAttribute("activePage", "phieu-xuat");
        return "phieuxuat";
    }

    @PostMapping("/phieuxuat/them")
    public String taoPhieuXuat(
            @RequestParam(required = false, defaultValue = "") String nguoiNhan,
            @RequestParam(required = false) List<String> sanPhamId,
            @RequestParam(required = false) List<String> soLuong,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            List<ChiTietPhieuXuatRequest> dong = new ArrayList<>();
            if (sanPhamId != null) {
                for (int i = 0; i < sanPhamId.size(); i++) {
                    String sp = sanPhamId.get(i);
                    if (sp == null || sp.isBlank()) {
                        continue;
                    }
                    ChiTietPhieuXuatRequest r = new ChiTietPhieuXuatRequest();
                    r.setSanPhamId(Long.parseLong(sp.trim()));
                    String sl = (soLuong != null && i < soLuong.size() && soLuong.get(i) != null) ? soLuong.get(i).trim() : "0";
                    r.setSoLuong(Integer.parseInt(sl.isEmpty() ? "0" : sl));
                    dong.add(r);
                }
            }

            PhieuXuatRequest request = new PhieuXuatRequest();
            request.setNguoiNhan(nguoiNhan);
            request.setNguoiTaoEmail(principal.getName());
            request.setChiTietList(dong);

            PhieuXuat px = phieuXuatService.taoPhieuXuat(request);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong",
                    "Đã tạo phiếu xuất #" + px.getId() + " cho " + px.getNguoiNhan() + " (xuất theo FEFO).");
        } catch (NumberFormatException e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi",
                    "Không thể tạo phiếu xuất: sản phẩm hoặc số lượng không hợp lệ.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Không thể tạo phiếu xuất: " + e.getMessage());
        }
        return "redirect:/phieuxuat";
    }
}
