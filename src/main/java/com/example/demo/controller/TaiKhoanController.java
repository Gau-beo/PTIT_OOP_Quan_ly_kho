package com.example.demo.controller;

import com.example.demo.service.NguoiDungService;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;
import java.security.Principal;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TaiKhoanController {

    private final NguoiDungService service;

    public TaiKhoanController(NguoiDungService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String hoTen,
            @RequestParam String email,
            @RequestParam String matKhau,
            @RequestParam String diaChi,
            Model model) {

        if (matKhau == null || matKhau.isBlank() || matKhau.length() < 6) {
            model.addAttribute("loi", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "auth/register";
        }

        if (service.kiemTraEmail(email)) {
            model.addAttribute("loi", "Email đã tồn tại!");
            return "auth/register";
        }

        service.dangKy(
                hoTen,
                email,
                matKhau,
                diaChi
        );

        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String admin() {
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("activePage", "admin");
        return "admin/dashboard";
    }

    @GetMapping({"/quan-ly", "/quan-ly/dashboard"})
    public String quanLyDashboard(Model model) {
        model.addAttribute("activePage", "quan-ly");
        return "quan-ly/dashboard";
    }

    @GetMapping({"/user", "/nguoi-dung", "/nguoi-dung/dashboard"})
    public String user(Model model) {
        model.addAttribute("activePage", "nguoi-dung");
        return "nguoi-dung/dashboard";
    }

    @PostMapping("/admin/xoa/{id}")
    public String xoaTaiKhoan(
            @PathVariable long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            service.xoaTaiKhoan(id, principal != null ? principal.getName() : "");
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã xóa tài khoản ID " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", e.getMessage());
        }
        return "redirect:/admin/tai-khoan/danh-sach";
    }
}
