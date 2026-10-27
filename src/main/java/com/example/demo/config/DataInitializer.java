package com.example.demo.config;

import java.time.LocalDate;
import java.util.function.Consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.*;
import com.example.demo.factory.SanPhamFactory;
import com.example.demo.repository.LoHangRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.NhaCungCapRepository;
import com.example.demo.repository.ProductRepository;

/**
 * Nạp dữ liệu mẫu khi khởi động. Mọi thao tác đều "idempotent": chạy lại nhiều lần không tạo trùng.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final LoHangRepository loHangRepository;
    private final NguoiDungRepository nguoiDungRepository;
    private final NhaCungCapRepository nhaCungCapRepository;
    private final PasswordEncoder passwordEncoder;
    private final SanPhamFactory sanPhamFactory;

    public DataInitializer(ProductRepository productRepository,
                           LoHangRepository loHangRepository,
                           NguoiDungRepository nguoiDungRepository,
                           NhaCungCapRepository nhaCungCapRepository,
                           PasswordEncoder passwordEncoder,
                           SanPhamFactory sanPhamFactory) {
        this.productRepository = productRepository;
        this.loHangRepository = loHangRepository;
        this.nguoiDungRepository = nguoiDungRepository;
        this.nhaCungCapRepository = nhaCungCapRepository;
        this.passwordEncoder = passwordEncoder;
        this.sanPhamFactory = sanPhamFactory;
    }

    @Override
    public void run(String... args) {
        // 0. Dữ liệu cũ (lớp Product thường) -> chuyển thành THUC_PHAM vì Product nay là lớp trừu tượng
        int daChuyen = productRepository.chuyenLoaiCu();
        if (daChuyen > 0) {
            System.out.println("===> [DataInitializer] Đã chuyển " + daChuyen + " sản phẩm cũ sang loại THUC_PHAM");
        }

        // 1. Tài khoản ADMIN mặc định
        if (!nguoiDungRepository.existsByEmail("admin@ptit.edu.vn")) {
            nguoiDungRepository.save(new TaiKhoanAdmin(
                    "Quản trị viên", "admin@ptit.edu.vn",
                    passwordEncoder.encode("admin123"), "ADMIN", "Quản lý kho"));
            System.out.println("===> [DataInitializer] Đã tạo tài khoản admin@ptit.edu.vn / admin123");
        }

        // 2. Nhà cung cấp mẫu
        if (nhaCungCapRepository.count() == 0) {
            taoNcc("Vinamilk Việt Nam", "10 Tân Trào, Quận 7, TP.HCM", "02854155555");
            taoNcc("Acecook Việt Nam", "Lô II-3, KCN Tân Bình, TP.HCM", "02838154064");
            taoNcc("Mondelez Kinh Đô", "138-142 Hai Bà Trưng, Quận 1, TP.HCM", "02838270838");
            taoNcc("Coca-Cola Beverages", "485 Hà Huy Tập, Thủ Đức, TP.HCM", "02838962345");
        }

        // 3. Sản phẩm mẫu (tạo qua Factory -> đủ 5 loại lớp con) + lô hàng mẫu
        LocalDate today = LocalDate.now();

        Product sp1 = timHoacTao(LoaiSanPham.THUC_PHAM, "SP001", "Sữa tươi tiệt trùng Vinamilk 1L", "Hộp", 35000.0,
                "Bảo quản nơi khô ráo, tránh ánh nắng trực tiếp. Nhiệt độ phòng.", null);
        Product sp2 = timHoacTao(LoaiSanPham.THUC_PHAM, "SP002", "Mì ăn liền Hảo Hảo Tôm Chua Cay", "Thùng", 125000.0,
                "Hàng dễ vỡ vụn khi vận chuyển, không xếp chồng quá 8 thùng.", null);
        Product sp3 = timHoacTao(LoaiSanPham.THUC_PHAM, "SP003", "Dầu ăn Simply 1L", "Chai", 58000.0,
                "Hàng chất lỏng, kiểm tra kỹ nắp chai trước khi nhập/xuất kho.", null);
        Product sp4 = timHoacTao(LoaiSanPham.THUC_PHAM, "SP004", "Bánh Cosy Mè Giòn 288g", "Gói", 42000.0,
                "Sản phẩm cận date cần ưu tiên xuất trước theo nguyên tắc FEFO.", null);
        Product sp5 = timHoacTao(LoaiSanPham.THUC_PHAM, "SP005", "Nước ngọt Coca-Cola 320ml", "Lon", 10000.0,
                "Hàng đóng lốc, kiểm tra hạn dập nổi trên đáy lon.", null);

        Product sp6 = timHoacTao(LoaiSanPham.DE_VO, "SP006", "Ly thủy tinh Luminarc (bộ 6)", "Hộp", 180000.0,
                "Hàng dễ vỡ, xếp tối đa 3 tầng, tránh va đập khi vận chuyển.", p -> {
                    SanPhamDeVo dv = (SanPhamDeVo) p;
                    dv.setMucDoDeVo("Cao");
                    dv.setYeuCauDongGoiDacBiet(true);
                });
        Product sp7 = timHoacTao(LoaiSanPham.HOA_CHAT, "SP007", "Nước rửa chén Sunlight 750g", "Chai", 32000.0,
                "Tránh xa tầm tay trẻ em, không để gần thực phẩm.", p ->
                        ((SanPhamHoaChat) p).setMucDoRuiRo("Thấp"));
        Product sp8 = timHoacTao(LoaiSanPham.DIEN_TU, "SP008", "Quạt điện Senko B1682", "Cái", 450000.0,
                "Tránh ẩm, kiểm tra tem bảo hành khi nhập kho.", p -> {
                    SanPhamDienTu dt = (SanPhamDienTu) p;
                    dt.setThoiGianBaoHanh(12);
                    dt.setCongSuat(50.0);
                });
        Product sp9 = timHoacTao(LoaiSanPham.CONG_KENH, "SP009", "Kệ sắt đa năng 5 tầng", "Bộ", 650000.0,
                "Hàng cồng kềnh, cần xe nâng hoặc 2 người khiêng.", p ->
                        ((SanPhamCongKenh) p).setPhuPhiVanChuyen(50000.0));

        taoLo("LH-VNM-01", today.minusMonths(2), today.plusMonths(4), 150, "Vinamilk Việt Nam", sp1);
        taoLo("LH-VNM-02", today.minusMonths(5), today.plusDays(15), 50, "Vinamilk Việt Nam", sp1);   // sắp hết hạn
        taoLo("LH-ACE-01", today.minusMonths(1), today.plusMonths(6), 300, "Acecook Việt Nam", sp2);
        taoLo("LH-SIM-01", today.minusWeeks(2), today.plusMonths(12), 200, "Cai Lan Oils", sp3);
        taoLo("LH-MON-01", today.minusMonths(8), today.minusDays(5), 25, "Mondelez Kinh Đô", sp4);    // đã hết hạn
        taoLo("LH-MON-02", today.minusWeeks(1), today.plusMonths(5), 120, "Mondelez Kinh Đô", sp4);
        taoLo("LH-COC-01", today.minusMonths(3), today.plusMonths(9), 500, "Coca-Cola Beverages", sp5);
        taoLo("LH-LUM-01", today.minusWeeks(3), null, 80, "Luminarc Việt Nam", sp6);                   // không có HSD
        taoLo("LH-SUN-01", today.minusMonths(1), today.plusYears(2), 150, "Unilever Việt Nam", sp7);
        taoLo("LH-SEN-01", today.minusWeeks(2), null, 40, "Senko Việt Nam", sp8);
        taoLo("LH-KES-01", today.minusDays(10), null, 20, "Xuân Hòa Việt Nam", sp9);

        System.out.println("===> [DataInitializer] Dữ liệu mẫu đã sẵn sàng!");
    }

    private Product timHoacTao(LoaiSanPham loai, String ma, String ten, String dvt, double gia,
                               String ghiChu, Consumer<Product> thuocTinhRieng) {
        return productRepository.findByMaSanPham(ma).orElseGet(() -> {
            Product p = sanPhamFactory.taoSanPham(loai, ma, ten, dvt, gia, ghiChu);
            if (thuocTinhRieng != null) {
                thuocTinhRieng.accept(p);
            }
            return productRepository.save(p);
        });
    }

    private void taoLo(String ma, LocalDate ngayNhap, LocalDate hanSuDung, int soLuong, String ncc, Product product) {
        if (!loHangRepository.existsByMaLoHang(ma)) {
            loHangRepository.save(new LoHang(ma, ngayNhap, hanSuDung, soLuong, ncc, product));
        }
    }

    private void taoNcc(String ten, String diaChi, String sdt) {
        NhaCungCap ncc = new NhaCungCap();
        ncc.setTenNCC(ten);
        ncc.setDiaChi(diaChi);
        ncc.setSdt(sdt);
        nhaCungCapRepository.save(ncc);
    }
}
