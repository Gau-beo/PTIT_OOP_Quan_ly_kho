package com.example.demo.controller;

import com.example.demo.dto.PhieuXuatRequest;
import com.example.demo.entity.PhieuXuat;
import com.example.demo.service.PhieuXuatService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/phieu-xuat")
public class PhieuXuatController {

    private final PhieuXuatService phieuXuatService;

    public PhieuXuatController(PhieuXuatService phieuXuatService) {
        this.phieuXuatService = phieuXuatService;
    }

    // Hiển thị trang form tạo phiếu xuất
    @GetMapping("/create")
    public String showFormTaoPhieuXuat(Model model) {
        if (!model.containsAttribute("phieuXuatRequest")) {
            model.addAttribute("phieuXuatRequest", new PhieuXuatRequest());
        }
        return "phieu-xuat/form"; // Trả về file src/main/resources/templates/phieu-xuat/form.html
    }

    // Xử lý submit form từ giao diện Thymeleaf
    @PostMapping("/create")
    public String taoPhieuXuat(@ModelAttribute("phieuXuatRequest") PhieuXuatRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            PhieuXuat phieuXuat = phieuXuatService.taoPhieuXuat(request);
            redirectAttributes.addFlashAttribute("successMessage", "Tạo phiếu xuất thành công! Mã phiếu: " + phieuXuat.getId());
            return "redirect:/phieu-xuat"; // Chuyển hướng về trang danh sách phiếu xuất
        } catch (IllegalArgumentException e) {
            // Lỗi nghiệp vụ (vd: thiếu hàng tồn kho) -> Giữ lại dữ liệu đã nhập và thông báo lỗi
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("phieuXuatRequest", request);
            return "redirect:/phieu-xuat/create";
        } catch (Exception e) {
            // Lỗi hệ thống khác
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            redirectAttributes.addFlashAttribute("phieuXuatRequest", request);
            return "redirect:/phieu-xuat/create";
        }
    }
}