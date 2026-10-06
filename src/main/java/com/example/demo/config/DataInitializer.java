package com.example.demo.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.LoHang;
import com.example.demo.entity.Product;
import com.example.demo.repository.LoHangRepository;
import com.example.demo.repository.ProductRepository;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final LoHangRepository loHangRepository;

    public DataInitializer(ProductRepository productRepository, LoHangRepository loHangRepository) {
        this.productRepository = productRepository;
        this.loHangRepository = loHangRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            // Khoi tao san pham mau
            Product sp1 = new Product("SP001", "Sữa tươi tiệt trùng Vinamilk 1L", "Hộp", 35000.0, "Bảo quản nơi khô ráo, tránh ánh nắng trực tiếp. Nhiệt độ phòng.");
            Product sp2 = new Product("SP002", "Mì ăn liền Hảo Hảo Tôm Chua Cay", "Thùng", 125000.0, "Hàng dễ vỡ vụn khi vận chuyển, không xếp chồng quá 8 thùng.");
            Product sp3 = new Product("SP003", "Dầu ăn Simply 1L", "Chai", 58000.0, "Hàng chất lỏng, kiểm tra kỹ nắp chai trước khi nhập/xuất kho.");
            Product sp4 = new Product("SP004", "Bánh Cosy Mè Giòn 288g", "Gói", 42000.0, "Sản phẩm cận date cần ưu tiên xuất trước theo nguyên tắc FEFO.");
            Product sp5 = new Product("SP005", "Nước ngọt Coca-Cola 320ml", "Lon", 10000.0, "Hàng đóng lốc, kiểm tra hạn dập nổi trên đáy lon.");

            productRepository.saveAll(List.of(sp1, sp2, sp3, sp4, sp5));

            // Khoi tao lo hang mau cho tung san pham voi cac tinh trang: Con han, Sap het han, Da het han
            LocalDate today = LocalDate.now();

            // Lo hang cho SP1 (Sữa tươi): 1 lo con han, 1 lo sap het han
            LoHang lh1 = new LoHang("LH-VNM-01", today.minusMonths(2), today.plusMonths(4), 150, "Vinamilk Việt Nam", sp1);
            LoHang lh2 = new LoHang("LH-VNM-02", today.minusMonths(5), today.plusDays(15), 50, "Vinamilk Việt Nam", sp1); // Sap het han (< 30 ngay)

            // Lo hang cho SP2 (Mì Hảo Hảo): 1 lo con han
            LoHang lh3 = new LoHang("LH-ACE-01", today.minusMonths(1), today.plusMonths(6), 300, "Acecook Việt Nam", sp2);

            // Lo hang cho SP3 (Dầu ăn Simply): 1 lo con han
            LoHang lh4 = new LoHang("LH-SIM-01", today.minusWeeks(2), today.plusMonths(12), 200, "Cai Lan Oils", sp3);

            // Lo hang cho SP4 (Bánh Cosy): 1 lo da het han, 1 lo con han
            LoHang lh5 = new LoHang("LH-MON-01", today.minusMonths(8), today.minusDays(5), 25, "Mondelez Kinh Đô", sp4); // Da het han
            LoHang lh6 = new LoHang("LH-MON-02", today.minusWeeks(1), today.plusMonths(5), 120, "Mondelez Kinh Đô", sp4);

            // Lo hang cho SP5 (Coca-Cola): 1 lo con han
            LoHang lh7 = new LoHang("LH-COC-01", today.minusMonths(3), today.plusMonths(9), 500, "Coca-Cola Beverages", sp5);

            loHangRepository.saveAll(List.of(lh1, lh2, lh3, lh4, lh5, lh6, lh7));

            System.out.println("===> [DataInitializer] Đã nạp thành công dữ liệu mẫu cho hệ thống Quản lý kho!");
        }
    }
}
