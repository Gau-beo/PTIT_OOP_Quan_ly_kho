# PTIT - HỆ THỐNG QUẢN LÝ KHO (OOP JAVA - SPRING BOOT)

Dự án đồ án môn Lập trình hướng đối tượng (OOP) / Phát triển ứng dụng Web tại Học viện Công nghệ Bưu chính Viễn thông (PTIT).

---

## 🛠 Công Nghệ Sử Dụng 

| Thành phần | Công nghệ |
| :--- | :--- |
| **Ngôn ngữ lập trình** | Java (JDK 21) |
| **Framework Backend** | Spring Boot (Spring Web, Spring Data JPA) |
| **Giao diện người dùng (Frontend)** | **Bootstrap 5** (UI/UX hiện đại, responsive) + **Thymeleaf Template Engine** |
| **Cơ sở dữ liệu** | MySQL |
| **Công cụ kiểm thử API** | Postman (Kèm file Postman Collection) |
| **Quản lý phiên bản mã nguồn** | Git & GitHub |

---

## 🚀 Các Tính Năng Nghiệp Vụ Chính 

### 1. 📝 Ghi Chú Sản Phẩm (`/san-pham`)
- Hiển thị danh sách sản phẩm cùng toàn bộ thông tin chi tiết (Mã SP, Tên SP, ĐVT, Đơn giá, Tồn kho).
- Hiển thị các ghi chú lưu kho, điều kiện bảo quản và vận chuyển của từng mặt hàng.
- Cho phép chỉnh sửa/cập nhật ghi chú trực tiếp qua Modal Bootstrap tương tác và lưu vào CSDL ngay lập tức.
- Cho phép thêm sản phẩm mới vào kho.

### 2. 🔍 Tra Cứu Lô Hàng (`/lo-hang`)
- Tìm kiếm linh hoạt theo **Mã lô hàng**, **Tên sản phẩm** hoặc **Nhà cung cấp**.
- Bộ lọc nâng cao theo trạng thái hạn dùng:
  - 🟢 **Còn hạn sử dụng**
  - 🟡 **Sắp hết hạn** (dưới 30 ngày)
  - 🔴 **Đã hết hạn**
- Bảng hiển thị thông tin lô hàng trực quan kèm nhãn (badge) trạng thái màu sắc.
- Hỗ trợ nhập thêm lô hàng mới qua Modal form.

### 3. 📊 Báo Cáo & Thống Kê (`/thong-ke` hoặc `/`)
- **4 Thẻ chỉ số tổng quan (KPI Cards):**
  - Tổng số loại sản phẩm trong kho.
  - Tổng số lô hàng đã nhập.
  - Tổng số lượng hàng hóa tồn kho.
  - Số lượng lô hàng cảnh báo (Hết hạn & Sắp hết hạn).
- **Bảng Cảnh Báo Lô Hàng Hết Hạn & Cận Date:** Tự động lọc và hiển thị danh sách các lô hàng có nguy cơ cao để ưu tiên xuất trước (FEFO) hoặc tiêu hủy.
- **Bảng Báo Cáo Tồn Kho Chi Tiết:** Đánh giá tình trạng tồn kho của từng sản phẩm (Đủ hàng, Sắp hết hàng, Hết hàng).

---

## ⚙️ Hướng Dẫn Cài Đặt & Chạy Ứng Dụng

### 1. Cấu hình Cơ sở dữ liệu (MySQL)
Mở file `src/main/resources/application.properties` và điều chỉnh tài khoản/mật khẩu MySQL nếu khác:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/oop_quan_ly_kho?createDatabaseIfNotExist=true&useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Ho_Chi_Minh&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=ptitoop
```
*(Lưu ý: Tham số `createDatabaseIfNotExist=true` sẽ tự động tạo database `oop_quan_ly_kho` nếu chưa có).*

### 2. Chạy ứng dụng
Mở terminal trong thư mục dự án và chạy:
```bash
./mvnw spring-boot:run
```
*(Hoặc click chuột phải vào file `QuanLyKhoApplication.java` trong Spring Tool Suite / Eclipse / IntelliJ IDEA và chọn **Run As -> Spring Boot App**).*

Khi ứng dụng khởi động lần đầu, hệ thống sẽ tự động nạp sẵn dữ liệu mẫu (`DataInitializer`) gồm 5 sản phẩm và 7 lô hàng với đầy đủ trạng thái để xem và thuyết trình ngay mà không cần mất công nhập liệu!

### 3. Truy cập giao diện Web
Mở trình duyệt web và truy cập:
- **Trang Báo cáo & Thống kê:** `http://localhost:8080/thong-ke` (hoặc `http://localhost:8080/`)
- **Trang Tra cứu lô hàng:** `http://localhost:8080/lo-hang`
- **Trang Ghi chú sản phẩm:** `http://localhost:8080/san-pham`

---

## 📮 Hướng Dẫn Kiểm Thử Bằng Postman
Trong thư mục dự án có sẵn file:
`PTIT_QuanLyKho_Postman_Collection.json`

1. Mở phần mềm **Postman**.
2. Chọn **Import** -> Chọn file `PTIT_QuanLyKho_Postman_Collection.json`.
3. Bộ sưu tập sẽ tự động tạo sẵn 7 API kiểm thử chuẩn RESTful:
   - `GET /api/products`: Lấy danh sách sản phẩm
   - `GET /api/products/{id}`: Xem chi tiết sản phẩm và ghi chú
   - `PUT /api/products/{id}/ghi-chu`: Cập nhật ghi chú sản phẩm
   - `GET /api/products/thong-ke`: Lấy báo cáo thống kê tổng quan
   - `GET /api/lo-hang`: Lấy tất cả lô hàng
   - `GET /api/lo-hang/tra-cuu?ma=...`: Tra cứu theo mã lô
   - `GET /api/lo-hang/tra-cuu-nang-cao?keyword=...&trangThai=...`: Tra cứu nâng cao
