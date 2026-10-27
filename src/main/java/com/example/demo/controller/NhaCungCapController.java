package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.NhaCungCap;
import com.example.demo.service.NhaCungCapService;

@Controller
@RequestMapping("/nha-cung-cap")
public class NhaCungCapController {

    // SỬA: dùng constructor injection + đi qua Service (giống các controller khác),
    // không gọi thẳng Repository bằng @Autowired trên field nữa.
    private final NhaCungCapService nhaCungCapService;

    public NhaCungCapController(NhaCungCapService nhaCungCapService) {
        this.nhaCungCapService = nhaCungCapService;
    }

    @GetMapping
    public String hienThiTrang(Model model) {
        model.addAttribute("danhSachNCC", nhaCungCapService.layTatCa());
        model.addAttribute("nhaCungCapMoi", new NhaCungCap());
        model.addAttribute("activePage", "nha-cung-cap");
        return "nhacungcap";
    }

    @PostMapping("/them")
    public String themNhaCungCap(NhaCungCap nhaCungCap, RedirectAttributes ra) {
        try {
            NhaCungCap daLuu = nhaCungCapService.them(nhaCungCap);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã thêm nhà cung cấp: " + daLuu.getTenNCC());
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", e.getMessage());
        }
        return "redirect:/nha-cung-cap";
    }

    @PostMapping("/sua/{id}")
    public String suaNhaCungCap(@PathVariable Integer id, NhaCungCap duLieuMoi, RedirectAttributes ra) {
        try {
            NhaCungCap daLuu = nhaCungCapService.capNhat(id, duLieuMoi);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã cập nhật nhà cung cấp: " + daLuu.getTenNCC());
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", e.getMessage());
        }
        return "redirect:/nha-cung-cap";
    }

    @PostMapping("/xoa/{id}")
    public String xoaNhaCungCap(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            nhaCungCapService.xoa(id);
            ra.addFlashAttribute("thongBaoThanhCong", "Đã xóa nhà cung cấp ID " + id);
        } catch (Exception e) {
            ra.addFlashAttribute("thongBaoLoi", e.getMessage());
        }
        return "redirect:/nha-cung-cap";
    }
}
