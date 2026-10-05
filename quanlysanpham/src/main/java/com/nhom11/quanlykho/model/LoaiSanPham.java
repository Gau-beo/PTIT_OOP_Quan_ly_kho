package com.nhom11.quanlykho.model;

/**
 * Liệt kê các loại sản phẩm con trong cây kế thừa của SanPham.
 * Giá trị này khớp với cột phân biệt (discriminator) "loai_san_pham"
 * trong bảng san_pham (chiến lược kế thừa SINGLE_TABLE).
 */
public enum LoaiSanPham {
    THUC_PHAM,
    DE_VO,
    HOA_CHAT,
    DIEN_TU,
    CONG_KENH
}
