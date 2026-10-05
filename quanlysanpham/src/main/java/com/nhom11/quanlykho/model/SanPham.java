package com.nhom11.quanlykho.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lớp cha trừu tượng cho toàn bộ cây kế thừa sản phẩm.
 * Dùng chiến lược SINGLE_TABLE: tất cả sản phẩm con lưu chung 1 bảng "san_pham",
 * phân biệt loại bằng cột "loai_san_pham" (discriminator).
 *
 * Mỗi lớp con (SanPhamThucPham, SanPhamDeVo, SanPhamHoaChat, SanPhamDienTu,
 * SanPhamCongKenh) chỉ cần bổ sung thuộc tính riêng và override getThongTinRieng().
 */
@Entity
@Table(name = "san_pham")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "loai_san_pham", discriminatorType = DiscriminatorType.STRING, length = 20)
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_san_pham", nullable = false)
    private String tenSanPham;

    @Column(name = "don_vi_tinh")
    private String donViTinh;

    @Column(name = "so_luong_ton", nullable = false)
    private Integer soLuongTon = 0;

    @Column(name = "nguong_canh_bao", nullable = false)
    private Integer nguongCanhBao = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "danh_muc_id")
    private DanhMuc danhMuc;

    protected SanPham(String tenSanPham, String donViTinh, Integer soLuongTon, Integer nguongCanhBao, DanhMuc danhMuc) {
        this.tenSanPham = tenSanPham;
        this.donViTinh = donViTinh;
        this.soLuongTon = soLuongTon;
        this.nguongCanhBao = nguongCanhBao;
        this.danhMuc = danhMuc;
    }

    /**
     * Quy tắc cảnh báo tồn kho dùng chung cho MỌI loại sản phẩm:
     * tồn kho hiện tại đã chạm hoặc dưới ngưỡng cảnh báo.
     */
    public boolean canCanhBaoTonKho() {
        return soLuongTon != null && nguongCanhBao != null && soLuongTon <= nguongCanhBao;
    }

    /**
     * Mỗi lớp con BẮT BUỘC mô tả thông tin riêng của mình (đa hình).
     */
    public abstract String getThongTinRieng();

    /**
     * Mỗi lớp con trả về loại của chính nó, dùng để hiển thị / lọc.
     */
    public abstract LoaiSanPham getLoaiSanPham();
}
