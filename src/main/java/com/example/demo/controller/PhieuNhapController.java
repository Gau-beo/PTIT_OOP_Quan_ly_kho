package com.example.demo.controller;

import com.example.demo.entity.PhieuNhap;
import com.example.demo.service.PhieuNhapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PhieuNhapController {

    @Autowired
    private PhieuNhapService phieuNhapService;

    @GetMapping("/phieunhap")
    public String viewPhieuNhapPage(Model model) {
        List<PhieuNhap> danhSach = phieuNhapService.getAllPhieuNhap();
        model.addAttribute("danhSachPhieuNhap", danhSach);
        return "phieunhap"; 
    }
}