package com.example.demo.service;

import com.example.demo.entity.TaiKhoanNguoiDung;
import com.example.demo.repository.NguoiDungRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class NguoiDungService {

    private final NguoiDungRepository repository;

    private final PasswordEncoder passwordEncoder;

    public NguoiDungService(
            NguoiDungRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    // Kiểm tra email đã tồn tại
    public boolean kiemTraEmail(String email) {

        return repository.existsByEmail(email);
    }

    // Đăng ký tài khoản người dùng
    public TaiKhoanNguoiDung dangKy(
            String hoTen,
            String email,
            String matKhau,
            String diaChi) {

        // Mã hóa mật khẩu
        String matKhauMaHoa =
                passwordEncoder.encode(matKhau);

        // Tạo tài khoản người dùng
        TaiKhoanNguoiDung nguoiDung =
                new TaiKhoanNguoiDung(
                        hoTen,
                        email,
                        matKhauMaHoa,
                        "USER",
                        diaChi
                );

        return repository.save(nguoiDung);
    }
}