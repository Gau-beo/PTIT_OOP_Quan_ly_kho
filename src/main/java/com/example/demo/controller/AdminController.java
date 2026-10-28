package com.example.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.NguoiDung;
import com.example.demo.service.NguoiDungService;

@Controller
@RequestMapping("/admin/tai-khoan")
public class AdminController {

    private final NguoiDungService nguoidungService;

    public AdminController(NguoiDungService nguoidungService) {
        this.nguoidungService = nguoidungService;
    }

    @GetMapping({ "", "/", "/danh-sach" })
    public String getDanhSach(Model model) {
        List<NguoiDung> danhSach = nguoidungService.layTatCa();
        model.addAttribute("danhSachTaiKhoan", danhSach);
        model.addAttribute("Danhsach", danhSach);
        model.addAttribute("activePage", "admin");
        return "admin/tai-khoan/danh-sach";
    }

    @GetMapping("/them")
    public String getThem(Model model) {
        model.addAttribute("activePage", "admin");
        return "admin/tai-khoan/them";
    }

    @PostMapping("/them")
    public String postThem(
            @RequestParam("hoTen") String hoTen,
            @RequestParam("email") String email,
            @RequestParam("matKhau") String matKhau,
            @RequestParam(value = "vaiTro", defaultValue = "USER") String vaiTro,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (nguoidungService.kiemTraEmail(email)) {
            model.addAttribute("thongBaoLoi", "Email đã tồn tại trong hệ thống!");
            model.addAttribute("error", "Email đã tồn tại trong hệ thống!");
            model.addAttribute("hoTen", hoTen);
            model.addAttribute("email", email);
            model.addAttribute("vaiTro", vaiTro);
            model.addAttribute("activePage", "admin");
            return "admin/tai-khoan/them";
        }

        if (matKhau == null || matKhau.isBlank() || matKhau.length() < 6) {
            model.addAttribute("thongBaoLoi", "Mật khẩu phải có ít nhất 6 ký tự!");
            model.addAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự!");
            model.addAttribute("hoTen", hoTen);
            model.addAttribute("email", email);
            model.addAttribute("vaiTro", vaiTro);
            model.addAttribute("activePage", "admin");
            return "admin/tai-khoan/them";
        }

        nguoidungService.themTaiKhoan(hoTen, email, matKhau, vaiTro);
        redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Thêm tài khoản thành công!");
        redirectAttributes.addFlashAttribute("success", "Thêm tài khoản thành công!");
        return "redirect:/admin/tai-khoan/danh-sach";
    }

    @GetMapping("/sua/{id}")
    public String getSua(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        var userOpt = nguoidungService.timTheoId(id);
        if (userOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", "Không tìm thấy tài khoản với ID: " + id);
            return "redirect:/admin/tai-khoan/danh-sach";
        }
        model.addAttribute("taiKhoan", userOpt.get());
        model.addAttribute("activePage", "admin");
        return "admin/tai-khoan/sua";
    }

    @PostMapping("/sua")
    public String postSua(
            @RequestParam("id") Long id,
            @RequestParam("hoTen") String hoTen,
            @RequestParam("email") String email,
            @RequestParam(value = "matKhau", required = false) String matKhau,
            @RequestParam("vaiTro") String vaiTro,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (nguoidungService.kiemTraEmailTrungLap(email, id)) {
            model.addAttribute("thongBaoLoi", "Email đã được sử dụng bởi tài khoản khác!");
            model.addAttribute("error", "Email đã được sử dụng bởi tài khoản khác!");
            var userOpt = nguoidungService.timTheoId(id);
            if (userOpt.isPresent()) {
                NguoiDung u = userOpt.get();
                u.setHoTen(hoTen);
                u.setEmail(email);
                u.setVaiTro(vaiTro);
                model.addAttribute("taiKhoan", u);
            }
            model.addAttribute("activePage", "admin");
            return "admin/tai-khoan/sua";
        }

        if (matKhau != null && !matKhau.isBlank() && matKhau.length() < 6) {
            model.addAttribute("thongBaoLoi", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            model.addAttribute("error", "Mật khẩu mới phải có ít nhất 6 ký tự!");
            var userOpt = nguoidungService.timTheoId(id);
            if (userOpt.isPresent()) {
                NguoiDung u = userOpt.get();
                u.setHoTen(hoTen);
                u.setEmail(email);
                u.setVaiTro(vaiTro);
                model.addAttribute("taiKhoan", u);
            }
            model.addAttribute("activePage", "admin");
            return "admin/tai-khoan/sua";
        }

        nguoidungService.capNhat(id, hoTen, email, matKhau, vaiTro);
        redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Cập nhật tài khoản thành công!");
        redirectAttributes.addFlashAttribute("success", "Cập nhật tài khoản thành công!");
        return "redirect:/admin/tai-khoan/danh-sach";
    }

    @PostMapping("/xoa/{id}")
    public String xoaTaiKhoanPost(
            @PathVariable("id") Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        return thucHienXoa(id, principal, redirectAttributes);
    }

    @GetMapping("/xoa/{id}")
    public String xoaTaiKhoanGet(
            @PathVariable("id") Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {
        return thucHienXoa(id, principal, redirectAttributes);
    }

    private String thucHienXoa(Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            String emailDangNhap = (principal != null) ? principal.getName() : "";
            nguoidungService.xoaTaiKhoan(id, emailDangNhap);
            redirectAttributes.addFlashAttribute("thongBaoThanhCong", "Đã xóa tài khoản ID: " + id);
            redirectAttributes.addFlashAttribute("success", "Đã xóa tài khoản ID: " + id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("thongBaoLoi", e.getMessage());
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/tai-khoan/danh-sach";
    }
}
