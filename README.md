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
| `/thong-ke` | Báo cáo tồn kho (tổng và khả dụng), cảnh báo lô hết hạn / sắp hết hạn (chỉ tính lô còn hàng) |
| `/lo-hang` | Tra cứu, lọc lô hàng (kể cả lô đã hết hàng); khai báo tồn đầu kỳ; **tiêu hủy** lô đã hết hạn (ADMIN) |
| `/san-pham` | Danh sách sản phẩm, thêm sản phẩm **theo loại**, **sửa** thông tin, **xóa** (chỉ khi chưa có lô hàng/phiếu nào) |
| `/phieunhap` | Tạo phiếu nhập nhiều dòng - tự sinh lô hàng mới (mã lô luôn duy nhất) |
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

## Quy tắc nghiệp vụ chính
- **Không xóa cứng dữ liệu đã phát sinh giao dịch** để giữ khả năng truy vết: lô hàng không có nút xóa (lô hết hạn được *tiêu hủy* - đưa số lượng về 0 và vẫn giữ lô); sản phẩm, nhà cung cấp, tài khoản chỉ xóa được khi chưa được tham chiếu ở đâu.
- Tồn kho **khả dụng** = tổng các lô chưa hết hạn; phiếu xuất chỉ xuất từ phần khả dụng này.
- Hạn sử dụng được quản lý theo **từng lô**; sản phẩm thực phẩm chỉ lưu thời hạn sử dụng tiêu chuẩn (tháng).
- Hàng nhập mới nên đi qua **Phiếu nhập** (có chứng từ); "Khai báo tồn đầu kỳ" ở trang Lô hàng chỉ dành cho hàng có sẵn trong kho.

## Hạn chế đã biết
- Chưa có cơ chế khóa chống xuất trùng khi nhiều người thao tác đồng thời cùng một sản phẩm.
- Chưa có unit test cho nghiệp vụ FEFO.
- Phiếu xuất chưa ghi lại cụ thể đã trừ từ lô nào.
