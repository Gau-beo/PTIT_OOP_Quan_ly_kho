package com.example.demo.service;

import com.example.demo.entity.TaiKhoanNguoiDung;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.PhieuXuatRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import com.example.demo.entity.NguoiDung;

@Service
public class NguoiDungService {

    private final NguoiDungRepository repository;

    private final PasswordEncoder passwordEncoder;
    private final PhieuXuatRepository phieuXuatRepository;

    public NguoiDungService(
            NguoiDungRepository repository,
            PasswordEncoder passwordEncoder,
            PhieuXuatRepository phieuXuatRepository) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.phieuXuatRepository = phieuXuatRepository;
    }

    @Transactional(readOnly = true)
    public List<NguoiDung> layTatCa() {
        return repository.findAll();
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

    /**
     * Admin xóa một tài khoản. Các chốt chặn:
     *  - không xóa tài khoản ADMIN (kể cả chính mình);
     *  - không xóa tài khoản đã từng lập phiếu xuất (để không mất dấu vết người thực hiện
     *    và tránh vi phạm khóa ngoại trong DB).
     */
    @Transactional
    public void xoaTaiKhoan(long id, String emailDangDangNhap) {
        NguoiDung nd = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy tài khoản ID: " + id));
        if ("ADMIN".equals(nd.getVaiTro())) {
            throw new IllegalArgumentException("Không thể xóa tài khoản quản trị (ADMIN)!");
        }
        if (nd.getEmail() != null && nd.getEmail().equalsIgnoreCase(emailDangDangNhap)) {
            throw new IllegalArgumentException("Bạn không thể tự xóa tài khoản đang đăng nhập!");
        }
        if (phieuXuatRepository.existsByNguoiTao_Id(id)) {
            throw new IllegalArgumentException("Không thể xóa '" + nd.getEmail()
                    + "' vì tài khoản này đã lập phiếu xuất.");
        }
        repository.delete(nd);
    }
}
