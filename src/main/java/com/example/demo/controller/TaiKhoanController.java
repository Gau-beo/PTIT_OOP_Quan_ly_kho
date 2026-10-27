package com.example.demo.controller;

import com.example.demo.service.NguoiDungService;

import org.springframework.stereotype.Controller;
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
}