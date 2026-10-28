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

    public boolean kiemTraEmail(String email) {

        return repository.existsByEmail(email);
    }

    public TaiKhoanNguoiDung dangKy(
            String hoTen,
            String email,
            String matKhau,
            String diaChi) {

        String matKhauMaHoa =
                passwordEncoder.encode(matKhau);

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

    public boolean kiemTraEmailTrungLap(String email, Long excludeId) {
        var existing = repository.findByEmail(email.trim());
        return existing.isPresent() && (excludeId == null || existing.get().getId() != excludeId.longValue());
    }

    public java.util.Optional<NguoiDung> timTheoId(Long id) {
        return repository.findById(id);
    }

    public NguoiDung themTaiKhoan(
            String hoTen,
            String email,
            String matKhau,
            String vaiTro) {

        String encodedPassword = passwordEncoder.encode(matKhau);
        String trimmedEmail = email.trim();
        NguoiDung nguoiDung;

        if ("ADMIN".equalsIgnoreCase(vaiTro)) {
            nguoiDung = new com.example.demo.entity.TaiKhoanAdmin(hoTen, trimmedEmail, encodedPassword, "ADMIN");
        } else if ("QUAN_LY".equalsIgnoreCase(vaiTro)) {
            nguoiDung = new com.example.demo.entity.TaiKhoanQuanLy(hoTen, trimmedEmail, encodedPassword, "QUAN_LY");
        } else {
            nguoiDung = new TaiKhoanNguoiDung(hoTen, trimmedEmail, encodedPassword, "USER", "");
        }

        return repository.save(nguoiDung);
    }

    @Transactional
    public NguoiDung capNhat(
            Long id,
            String hoTen,
            String email,
            String matKhau,
            String vaiTro) {

        NguoiDung nguoiDung = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng có ID: " + id));

        nguoiDung.setHoTen(hoTen);
        nguoiDung.setEmail(email.trim());
        nguoiDung.setVaiTro(vaiTro);

        if (matKhau != null && !matKhau.isBlank()) {
            nguoiDung.setMatKhau(passwordEncoder.encode(matKhau));
        }

        return repository.save(nguoiDung);
    }

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
