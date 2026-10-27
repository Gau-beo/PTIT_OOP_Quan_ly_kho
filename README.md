# PTIT - Hệ thống Quản lý Kho (OOP)

Ứng dụng web quản lý kho viết bằng **Spring Boot + Spring Data JPA + Thymeleaf + Bootstrap 5**, CSDL **MySQL**.
Giao diện render phía server (Thymeleaf), **không dùng REST API**.

## Chạy dự án
1. Cài MySQL, sửa `spring.datasource.username/password` trong `src/main/resources/application.properties`.
2. Chạy `QuanLyKhoApplication` (hoặc `mvnw spring-boot:run`). Bảng được tự tạo (`ddl-auto=update`, dữ liệu được giữ nguyên).
3. Mở http://localhost:8080 - tài khoản ADMIN mặc định: `admin@ptit.edu.vn` / `admin123`.
   Người dùng thường tự đăng ký ở trang `/register`.

## Chức năng
| Trang | Mô tả |
|---|---|
| `/thong-ke` | Báo cáo tồn kho, cảnh báo lô hết hạn / sắp hết hạn |
| `/lo-hang` | Tra cứu, lọc lô hàng; nhập lô mới |
| `/san-pham` | Danh sách sản phẩm, thêm sản phẩm **theo loại**, sửa ghi chú |
| `/phieunhap` | Tạo phiếu nhập nhiều dòng - tự sinh lô hàng mới |
| `/phieuxuat` | Tạo phiếu xuất nhiều dòng - tự trừ tồn theo **FEFO**, bỏ qua lô hết hạn |
| `/nha-cung-cap` | Quản lý nhà cung cấp |
| `/admin` | (chỉ ADMIN) danh sách tài khoản hệ thống |

## Tính chất OOP thể hiện trong code
- **Đóng gói**: thuộc tính `private`, truy cập qua getter/setter; logic nghiệp vụ nằm trong entity (`LoHang.isHetHan()`, `Product.getTongTonKho()`).
- **Kế thừa**: `Product` (abstract) -> `SanPhamThucPham`, `SanPhamDeVo`, `SanPhamHoaChat`, `SanPhamDienTu`, `SanPhamCongKenh`;
  `NguoiDung` -> `TaiKhoanAdmin`, `TaiKhoanNguoiDung`. Ánh xạ JPA: SINGLE_TABLE (sản phẩm) và JOINED (người dùng).
- **Trừu tượng**: `Product.getThongTinRieng()` và `getLoaiSanPham()` là phương thức abstract.
- **Đa hình**: mỗi lớp con ghi đè `getThongTinRieng()` / `getMoTaTaiKhoan()`; giao diện chỉ gọi qua lớp cha.
- **Factory Pattern**: `SanPhamFactory` tạo đúng lớp con theo `LoaiSanPham`.
- **Phân tầng**: Controller -> Service -> Repository -> Entity.
