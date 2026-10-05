# Module Quản lý sản phẩm

Package gốc: `com.nhom11.quanlykho` — **đổi lại theo package thật của project nhóm** bằng
find & replace `com.nhom11.quanlykho` -> package của bạn, rồi copy các thư mục
`model/repository/dto/service/factory/controller/exception` vào project Spring Boot hiện có.

## 1. Cấu trúc

```
model/
  SanPham.java            (abstract, lớp cha - kế thừa SINGLE_TABLE)
  SanPhamThucPham.java    (+ ngayHetHan)
  SanPhamDeVo.java        (+ mucDoDeVo, yeuCauDongGoiDacBiet)
  SanPhamHoaChat.java     (+ mucDoRuiRo)
  SanPhamDienTu.java      (+ thoiGianBaoHanh, congSuat)
  SanPhamCongKenh.java    (+ phuPhiVanChuyen)
  DanhMuc.java
  LoaiSanPham.java        (enum)
repository/    SanPhamRepository, DanhMucRepository
dto/           SanPhamRequestDTO, SanPhamResponseDTO
factory/       SanPhamFactory        (Factory Pattern: khởi tạo đúng lớp con)
service/       SanPhamService, DanhMucService (+ impl/)
controller/    SanPhamController, DanhMucController
exception/     ResourceNotFoundException, GlobalExceptionHandler
```

## 2. Điểm OOP cần nhấn khi báo cáo

- **Kế thừa**: `SanPham` (abstract) là lớp cha, 5 lớp con kế thừa và bổ sung
  thuộc tính riêng.
- **Đa hình**: `getThongTinRieng()` và `getLoaiSanPham()` là abstract method,
  mỗi lớp con override khác nhau; `SanPhamServiceImpl.chuyenSangDTO()` gọi qua
  biến kiểu `SanPham` nhưng JVM tự chạy đúng bản override.
- **Trừu tượng hóa**: `canCanhBaoTonKho()` định nghĩa 1 lần ở lớp cha, dùng
  chung cho mọi loại sản phẩm.
- **Factory Pattern**: `SanPhamFactory` là nơi duy nhất biết cách new ra từng
  lớp con — Service không cần biết chi tiết constructor.
- **Đóng gói (Single Table Inheritance)**: cả cây kế thừa lưu chung 1 bảng
  `san_pham`, cột `loai_san_pham` phân biệt loại — khớp với ERD hiện có của nhóm.

## 3. Cập nhật ERD

ERD hiện tại chưa có cột cho loại "Dễ vỡ". Cần thêm vào bảng `SanPham` trong
sơ đồ chung của nhóm:
- `mucDoDeVo` (varchar)
- `yeuCauDongGoiDacBiet` (boolean)

## 4. Dependency cần có trong `pom.xml`

Nếu project chưa có sẵn (thường Spring Initializr đã có `spring-boot-starter-web`,
`spring-boot-starter-data-jpa`, driver MySQL), thêm 2 cái sau nếu thiếu:

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

## 5. Ví dụ gọi API (mỗi loại 1 request mẫu)

**POST /api/san-pham** — Thực phẩm:
```json
{
  "loaiSanPham": "THUC_PHAM",
  "tenSanPham": "Sữa tươi Vinamilk 1L",
  "donViTinh": "Hộp",
  "soLuongTon": 20,
  "nguongCanhBao": 10,
  "danhMucId": 1,
  "ngayHetHan": "2026-11-01"
}
```

**POST /api/san-pham** — Dễ vỡ:
```json
{
  "loaiSanPham": "DE_VO",
  "tenSanPham": "Ly thủy tinh Luminarc",
  "donViTinh": "Bộ",
  "soLuongTon": 5,
  "nguongCanhBao": 8,
  "danhMucId": 2,
  "mucDoDeVo": "CAO",
  "yeuCauDongGoiDacBiet": true
}
```

**POST /api/san-pham** — Hóa chất:
```json
{
  "loaiSanPham": "HOA_CHAT",
  "tenSanPham": "Nước tẩy rửa công nghiệp",
  "donViTinh": "Can 5L",
  "soLuongTon": 15,
  "nguongCanhBao": 5,
  "danhMucId": 3,
  "mucDoRuiRo": "TRUNG_BINH"
}
```

**POST /api/san-pham** — Điện tử:
```json
{
  "loaiSanPham": "DIEN_TU",
  "tenSanPham": "Quạt điện Panasonic",
  "donViTinh": "Cái",
  "soLuongTon": 12,
  "nguongCanhBao": 4,
  "danhMucId": 4,
  "thoiGianBaoHanh": 24,
  "congSuat": 45.5
}
```

**POST /api/san-pham** — Cồng kềnh:
```json
{
  "loaiSanPham": "CONG_KENH",
  "tenSanPham": "Tủ lạnh Samsung 300L",
  "donViTinh": "Cái",
  "soLuongTon": 3,
  "nguongCanhBao": 5,
  "danhMucId": 5,
  "phuPhiVanChuyen": 150000
}
```

**GET /api/san-pham/canh-bao-ton-kho** — trả về tất cả sản phẩm (bất kể loại nào)
đang có `soLuongTon <= nguongCanhBao`. Trong ví dụ trên, Dễ vỡ (5<=8) và Cồng
kềnh (3<=5) sẽ xuất hiện trong danh sách này.

## 6. Test bằng Postman (đúng công nghệ nhóm đã chọn)

Import 3 request POST mẫu ở trên -> Send -> lấy `id` trả về -> test tiếp
GET/PUT/DELETE theo id đó. Sau đó gọi `GET /api/san-pham/canh-bao-ton-kho`
để kiểm tra tính năng cảnh báo.
