package com.example.demo.controller;

import com.example.demo.entity.NhaCungCap;
import com.example.demo.repository.NhaCungCapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nha-cung-cap")
public class NhaCungCapController {

    @Autowired
    private NhaCungCapRepository nhaCungCapRepo;

    @GetMapping
    public String hienThiTrang(Model model) {
        model.addAttribute("danhSachNCC", nhaCungCapRepo.findAll());
        
        model.addAttribute("nhaCungCapMoi", new NhaCungCap());
        
        return "nhacungcap";
    }

    @PostMapping("/them")
    public String themNhaCungCap(NhaCungCap nhaCungCap) {
        nhaCungCapRepo.save(nhaCungCap); 
        return "redirect:/nha-cung-cap";
    }
}