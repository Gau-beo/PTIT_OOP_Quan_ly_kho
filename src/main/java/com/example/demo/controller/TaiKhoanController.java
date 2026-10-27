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

    public TaiKhoanController(
            NguoiDungService service) {

        this.service = service;
    }


    // =========================
    // LOGIN
    // =========================

    // GET /login
    @GetMapping("/login")
    public String login() {

        return "login";
    }


    // =========================
    // REGISTER
    // =========================

    // GET /register
    @GetMapping("/register")
    public String register() {

        return "register";
    }


    // POST /register
    @PostMapping("/register")
    public String register(
            @RequestParam String hoTen,
            @RequestParam String email,
            @RequestParam String matKhau,
            @RequestParam String diaChi,
            Model model) {

        // Kiểm tra độ dài mật khẩu (phía server - không thể bị bỏ qua như kiểm tra trên trình duyệt)
        if (matKhau == null || matKhau.isBlank() || matKhau.length() < 6) {
            model.addAttribute("loi", "Mật khẩu phải có ít nhất 6 ký tự!");
            return "register";
        }

        // Kiểm tra email
        if (service.kiemTraEmail(email)) {

            model.addAttribute(
                    "loi",
                    "Email đã tồn tại!"
            );

            return "register";
        }

        // Lưu tài khoản
        service.dangKy(
                hoTen,
                email,
                matKhau,
                diaChi
        );

        return "redirect:/login";
    }


    // =========================
    // HOME
    // =========================

    @GetMapping("/home")
    public String home() {

        return "home";
    }


    // =========================
    // ADMIN
    // =========================

    @GetMapping("/admin")
    public String admin(Model model) {

        model.addAttribute("danhSachTaiKhoan", service.layTatCa());
        model.addAttribute("activePage", "admin");

        return "admin";
    }


    // =========================
    // USER
    // =========================

    @GetMapping("/user")
    public String user() {

        return "user";
    }

    // POST /admin/xoa/{id}  (đường dẫn /admin/** đã được SecurityConfig giới hạn cho ADMIN)
    @PostMapping("/admin/xoa/{id}")
    public String xoaTaiKhoan(
            @PathVariable long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            service.xoaTaiKhoan(id, principal.getName());
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã xóa tài khoản ID " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", e.getMessage());
        }
        return "redirect:/admin";
    }
}
