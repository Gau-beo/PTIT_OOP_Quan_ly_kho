'use strict';

/* ==========================================================
   Quản lý sản phẩm: logic giao diện
   Gọi REST API của Spring Boot (cùng origin, không cần CORS):
     /api/san-pham, /api/san-pham/danh-muc/{id}, /api/san-pham/canh-bao-ton-kho
     /api/danh-muc
   ========================================================== */

const API = { sanPham: '/api/san-pham', danhMuc: '/api/danh-muc' };

/* ---------- Cấu hình loại sản phẩm (khớp enum LoaiSanPham ở backend) ---------- */

const MUC_DO = [
  { value: 'THAP', label: 'Thấp' },
  { value: 'TRUNG_BINH', label: 'Trung bình' },
  { value: 'CAO', label: 'Cao' },
];

// Mỗi loại: nhãn hiển thị + các trường riêng (khớp thuộc tính của từng lớp con)
const LOAI = {
  THUC_PHAM: {
    label: 'Thực phẩm',
    fields: [{ key: 'ngayHetHan', label: 'Ngày hết hạn', type: 'date' }],
  },
  DE_VO: {
    label: 'Dễ vỡ',
    fields: [
      { key: 'mucDoDeVo', label: 'Mức độ dễ vỡ', type: 'select', options: MUC_DO },
      { key: 'yeuCauDongGoiDacBiet', label: 'Cần đóng gói đặc biệt', type: 'checkbox' },
    ],
  },
  HOA_CHAT: {
    label: 'Hóa chất',
    fields: [{ key: 'mucDoRuiRo', label: 'Mức độ rủi ro', type: 'select', options: MUC_DO }],
  },
  DIEN_TU: {
    label: 'Điện tử',
    fields: [
      { key: 'thoiGianBaoHanh', label: 'Bảo hành (tháng)', type: 'number', step: '1' },
      { key: 'congSuat', label: 'Công suất (W)', type: 'number', step: 'any' },
    ],
  },
  CONG_KENH: {
    label: 'Cồng kềnh',
    fields: [{ key: 'phuPhiVanChuyen', label: 'Phụ phí vận chuyển (đ)', type: 'number', step: 'any' }],
  },
};

// Ký hiệu xếp dỡ cho từng loại (viewBox 24x24, nét vẽ bằng currentColor)
const ICONS = {
  THUC_PHAM: '<path d="M5 19c0-8.5 5.5-13.5 14-14.5 0 9-5 14.5-13.5 14.5"/><path d="M5 19c2.5-4.5 5.5-7.5 9.5-9.5"/>',
  DE_VO: '<path d="M7 3h10l-1.2 6.2a3.8 3.8 0 0 1-7.6 0L7 3z"/><path d="M12 13v6M8 20h8"/>',
  HOA_CHAT: '<path d="M12 2.5l9.5 9.5-9.5 9.5L2.5 12 12 2.5z"/><path d="M12 8v5M12 15.8v.2"/>',
  DIEN_TU: '<path d="M13 2L5 13h6l-1 9 8-11h-6l1-9z"/>',
  CONG_KENH: '<path d="M3 8l9-5 9 5v8l-9 5-9-5V8z"/><path d="M3 8l9 5 9-5M12 13v8"/>',
};

/* ---------- Trạng thái ---------- */

const state = {
  products: [],
  categories: [],
  alerts: [],
  filter: { danhMucId: '', loai: '', keyword: '' },
  editingProductId: null,
  editingCategoryId: null,
};

/* ---------- Tiện ích ---------- */

const $ = (sel, root = document) => root.querySelector(sel);
const $$ = (sel, root = document) => Array.from(root.querySelectorAll(sel));

function esc(value) {
  return String(value ?? '').replace(/[&<>"']/g, (c) => (
    { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]
  ));
}

// Bỏ dấu tiếng Việt để tìm kiếm không phân biệt dấu
const fold = (s) => String(s ?? '').toLowerCase().normalize('NFD')
  .replace(/[̀-ͯ]/g, '').replace(/đ/g, 'd');

const loaiLabel = (loai) => (LOAI[loai] ? LOAI[loai].label : String(loai ?? ''));

function toast(message, type = 'ok') {
  const el = document.createElement('div');
  el.className = `toast ${type === 'error' ? 'error' : ''}`;
  el.textContent = message;
  $('#toasts').appendChild(el);
  setTimeout(() => el.classList.add('out'), type === 'error' ? 5000 : 2600);
  setTimeout(() => el.remove(), type === 'error' ? 5400 : 3000);
}

function showBanner(message) {
  const el = $('#banner');
  el.textContent = message;
  el.hidden = !message;
}

async function api(url, options = {}) {
  let res;
  try {
    res = await fetch(url, { headers: { 'Content-Type': 'application/json' }, ...options });
  } catch (err) {
    throw new Error('Không kết nối được tới máy chủ. Kiểm tra backend Spring Boot đang chạy rồi thử lại.');
  }
  if (!res.ok) {
    let message = `Yêu cầu thất bại (mã ${res.status}).`;
    try {
      const body = await res.json();
      if (body && body.message) message = body.message;
    } catch (_) { /* thân phản hồi không phải JSON */ }
    throw new Error(message);
  }
  return res.status === 204 ? null : res.json();
}

function stamp(loai) {
  return `<span class="stamp t-${esc(loai)}" aria-hidden="true"><svg viewBox="0 0 24 24">${ICONS[loai] || ''}</svg></span>`;
}

function kind(loai) {
  return `<span class="kind t-${esc(loai)}">${stamp(loai)}<span>${esc(loaiLabel(loai))}</span></span>`;
}

function gauge(p) {
  // Thang đo: gấp đôi ngưỡng (hoặc tồn hiện tại nếu lớn hơn); vạch đen đánh dấu ngưỡng cảnh báo
  const max = Math.max(p.nguongCanhBao * 2, p.soLuongTon, 1);
  const pct = Math.min(100, (p.soLuongTon / max) * 100);
  const tick = Math.min(100, (p.nguongCanhBao / max) * 100);
  const level = p.soLuongTon === 0 ? 'out' : (p.canCanhBaoTonKho ? 'low' : 'ok');
  return `<span class="gauge ${level}" role="img" aria-label="Tồn ${p.soLuongTon}, ngưỡng cảnh báo ${p.nguongCanhBao}">`
    + `<i style="width:${pct}%"></i><b style="left:${tick}%"></b></span>`;
}

function stockFlag(p) {
  if (p.soLuongTon === 0) return '<span class="flag out">Hết hàng</span>';
  if (p.canCanhBaoTonKho) return '<span class="flag low">Sắp hết</span>';
  return '';
}

function formatInfo(text) {
  // Backend trả chuỗi thô: ngày yyyy-MM-dd, mức độ THAP/TRUNG_BINH/CAO, tiền không có dấu phân cách.
  // Đổi sang dạng dễ đọc cho người dùng.
  return String(text ?? '')
    .replace(/(\d{4})-(\d{2})-(\d{2})/g, '$3/$2/$1')
    .replace(/\bTRUNG_BINH\b/g, 'Trung bình')
    .replace(/\bTHAP\b/g, 'Thấp')
    .replace(/\bCAO\b/g, 'Cao')
    .replace(/(\d{4,})( đ)/g, (_, n, unit) => Number(n).toLocaleString('vi-VN') + unit);
}

/* ---------- Tải dữ liệu ---------- */

async function loadCategories() {
  state.categories = await api(API.danhMuc);
  renderCategoryFilter();
  renderCategories();
}

async function loadProducts() {
  const id = state.filter.danhMucId;
  state.products = await api(id ? `${API.sanPham}/danh-muc/${id}` : API.sanPham);
  renderProducts();
}

async function loadAlerts() {
  state.alerts = await api(`${API.sanPham}/canh-bao-ton-kho`);
  renderAlerts();
}

const refreshProducts = () => Promise.all([loadProducts(), loadAlerts()]);

/* ---------- Hiển thị: sản phẩm ---------- */

function visibleProducts() {
  const kw = fold(state.filter.keyword.trim());
  return state.products.filter((p) => (
    (!state.filter.loai || p.loaiSanPham === state.filter.loai)
    && (!kw || fold(p.tenSanPham).includes(kw))
  ));
}

function productRow(p) {
  const info = formatInfo(p.thongTinRieng);
  const expired = info.includes('ĐÃ HẾT HẠN');
  const rowClass = p.soLuongTon === 0 ? 'row-out' : (p.canCanhBaoTonKho ? 'row-warn' : '');
  return `<tr class="${rowClass}">
    <td class="col-id">${p.id}</td>
    <td><span class="name">${esc(p.tenSanPham)}</span>${p.donViTinh ? `<span class="sub">${esc(p.donViTinh)}</span>` : ''}</td>
    <td>${kind(p.loaiSanPham)}</td>
    <td>${p.tenDanhMuc ? esc(p.tenDanhMuc) : '<span class="muted">Chưa phân loại</span>'}</td>
    <td class="col-stock"><div class="stock"><span class="qty">${p.soLuongTon}</span>${stockFlag(p)}</div>${gauge(p)}</td>
    <td class="num">${p.nguongCanhBao}</td>
    <td><span class="${expired ? 'info-bad' : ''}">${esc(info)}</span></td>
    <td class="col-act">
      <button type="button" class="link-btn" data-action="edit" data-id="${p.id}">Sửa</button>
      <button type="button" class="link-btn danger" data-action="delete" data-id="${p.id}">Xóa</button>
    </td>
  </tr>`;
}

function renderProducts() {
  const list = visibleProducts();
  $('#productRows').innerHTML = list.map(productRow).join('');

  const empty = $('#productEmpty');
  if (list.length === 0) {
    empty.hidden = false;
    empty.textContent = state.products.length === 0 && !state.filter.danhMucId
      ? 'Chưa có sản phẩm nào. Chọn Thêm sản phẩm để tạo sản phẩm đầu tiên.'
      : 'Không có sản phẩm khớp bộ lọc. Thử bỏ bớt bộ lọc.';
  } else {
    empty.hidden = true;
  }

  $('#productNote').textContent = state.products.length
    ? `Hiển thị ${list.length} trên ${state.products.length} sản phẩm`
    : '';
}

function renderCategoryFilter() {
  const sel = $('#filterDanhMuc');
  const current = state.filter.danhMucId;
  sel.innerHTML = '<option value="">Tất cả danh mục</option>'
    + state.categories.map((c) => `<option value="${c.id}">${esc(c.tenDanhMuc)}</option>`).join('');
  sel.value = state.categories.some((c) => String(c.id) === String(current)) ? current : '';
  state.filter.danhMucId = sel.value;
}

function renderLoaiFilter() {
  $('#filterLoai').innerHTML = '<option value="">Tất cả loại</option>'
    + Object.entries(LOAI).map(([k, v]) => `<option value="${k}">${esc(v.label)}</option>`).join('');
}

/* ---------- Hiển thị: danh mục ---------- */

function renderCategories() {
  $('#categoryRows').innerHTML = state.categories.map((c) => `<tr>
    <td class="col-id">${c.id}</td>
    <td><span class="name">${esc(c.tenDanhMuc)}</span></td>
    <td class="col-act">
      <button type="button" class="link-btn" data-action="edit-cat" data-id="${c.id}">Sửa</button>
      <button type="button" class="link-btn danger" data-action="delete-cat" data-id="${c.id}">Xóa</button>
    </td>
  </tr>`).join('');
  $('#categoryEmpty').hidden = state.categories.length > 0;
}

/* ---------- Hiển thị: cảnh báo tồn kho ---------- */

function renderAlerts() {
  const sorted = [...state.alerts].sort((a, b) => {
    const ra = a.nguongCanhBao ? a.soLuongTon / a.nguongCanhBao : 0;
    const rb = b.nguongCanhBao ? b.soLuongTon / b.nguongCanhBao : 0;
    return (a.soLuongTon === 0 ? -1 : 0) - (b.soLuongTon === 0 ? -1 : 0) || ra - rb;
  });

  $('#alertRows').innerHTML = sorted.map((p) => `<tr class="${p.soLuongTon === 0 ? 'row-out' : 'row-warn'}">
    <td><span class="name">${esc(p.tenSanPham)}</span>${p.donViTinh ? `<span class="sub">${esc(p.donViTinh)}</span>` : ''}</td>
    <td>${kind(p.loaiSanPham)}</td>
    <td>${p.tenDanhMuc ? esc(p.tenDanhMuc) : '<span class="muted">Chưa phân loại</span>'}</td>
    <td class="col-stock">
      <div class="stock"><span class="qty">${p.soLuongTon}</span>${stockFlag(p)}</div>
      ${gauge(p)}
      <span class="gauge-note">Ngưỡng cảnh báo ${p.nguongCanhBao}</span>
    </td>
    <td class="col-act"><button type="button" class="link-btn" data-action="edit" data-id="${p.id}">Cập nhật tồn kho</button></td>
  </tr>`).join('');

  const n = sorted.length;
  const out = sorted.filter((p) => p.soLuongTon === 0).length;
  $('#alertEmpty').hidden = n > 0;
  $('#alertSummary').textContent = n === 0
    ? ''
    : `${n} sản phẩm đang chạm ngưỡng cảnh báo${out ? `, trong đó ${out} đã hết hàng` : ''}.`;

  const badge = $('#alertCount');
  badge.textContent = n;
  badge.hidden = n === 0;
}

/* ---------- Chuyển màn hình ---------- */

function showView(name) {
  $$('.tab').forEach((tab) => {
    const on = tab.dataset.view === name;
    tab.classList.toggle('is-active', on);
    if (on) tab.setAttribute('aria-current', 'page'); else tab.removeAttribute('aria-current');
  });
  $$('.view').forEach((v) => v.classList.toggle('is-active', v.id === `view-${name}`));
  if (name === 'canh-bao') loadAlerts().catch((e) => toast(e.message, 'error'));
}

/* ---------- Hộp thoại xác nhận ---------- */

function confirmAction({ title, message, okLabel }) {
  const dlg = $('#confirmDialog');
  $('#confirmTitle').textContent = title;
  $('#confirmMessage').textContent = message;
  $('#confirmOk').textContent = okLabel;
  return new Promise((resolve) => {
    const onClose = () => {
      dlg.removeEventListener('close', onClose);
      resolve(dlg.returnValue === 'ok');
    };
    dlg.addEventListener('close', onClose);
    dlg.returnValue = '';
    dlg.showModal();
  });
}

/* ---------- Form sản phẩm ---------- */

function fieldHtml(f, value) {
  const id = `tf_${f.key}`;
  if (f.type === 'checkbox') {
    return `<label class="check span-2"><input type="checkbox" id="${id}" ${value ? 'checked' : ''}> ${esc(f.label)}</label>`;
  }
  if (f.type === 'select') {
    const opts = f.options.map((o) => `<option value="${o.value}" ${o.value === value ? 'selected' : ''}>${esc(o.label)}</option>`).join('');
    return `<div class="field"><label for="${id}">${esc(f.label)}</label><select id="${id}"><option value="">Chưa chọn</option>${opts}</select></div>`;
  }
  const attrs = f.type === 'number' ? `min="0" step="${f.step || '1'}"` : '';
  return `<div class="field"><label for="${id}">${esc(f.label)}</label><input id="${id}" type="${f.type}" ${attrs} value="${esc(value ?? '')}"></div>`;
}

function renderTypeFields(loai, values = {}) {
  const cfg = LOAI[loai];
  $('#typeLegend').innerHTML = `<span class="kind t-${esc(loai)}">${stamp(loai)}<span>Thông tin riêng của loại ${esc(cfg.label)}</span></span>`;
  $('#typeFields').innerHTML = cfg.fields.map((f) => fieldHtml(f, values[f.key])).join('');
}

function fillCategorySelect(selectedId) {
  $('#f_dm').innerHTML = '<option value="">Chưa phân loại</option>'
    + state.categories.map((c) => `<option value="${c.id}">${esc(c.tenDanhMuc)}</option>`).join('');
  $('#f_dm').value = selectedId != null ? String(selectedId) : '';
}

function openProductForm(product = null) {
  const editing = product !== null;
  state.editingProductId = editing ? product.id : null;

  $('#productDialogTitle').textContent = editing ? 'Sửa sản phẩm' : 'Thêm sản phẩm';
  $('#productSubmit').textContent = editing ? 'Lưu thay đổi' : 'Lưu sản phẩm';

  const loaiSel = $('#f_loai');
  loaiSel.innerHTML = Object.entries(LOAI).map(([k, v]) => `<option value="${k}">${esc(v.label)}</option>`).join('');
  loaiSel.value = editing ? product.loaiSanPham : 'THUC_PHAM';
  loaiSel.disabled = editing; // backend không cho đổi loại sau khi tạo
  $('#loaiHint').hidden = !editing;

  $('#f_ten').value = editing ? product.tenSanPham : '';
  $('#f_dvt').value = editing ? (product.donViTinh ?? '') : '';
  $('#f_ton').value = editing ? product.soLuongTon : '0';
  $('#f_nguong').value = editing ? product.nguongCanhBao : '0';
  fillCategorySelect(editing ? product.danhMucId : null);
  renderTypeFields(loaiSel.value, editing ? product : {});

  $('#productDialog').showModal();
  $('#f_ten').focus();
}

function collectProductPayload() {
  const loai = $('#f_loai').value;
  const payload = {
    loaiSanPham: loai,
    tenSanPham: $('#f_ten').value.trim(),
    donViTinh: $('#f_dvt').value.trim() || null,
    soLuongTon: Number($('#f_ton').value),
    nguongCanhBao: Number($('#f_nguong').value),
    danhMucId: $('#f_dm').value ? Number($('#f_dm').value) : null,
  };
  // Chỉ gửi các trường riêng của đúng loại đang chọn
  LOAI[loai].fields.forEach((f) => {
    const el = $(`#tf_${f.key}`);
    if (f.type === 'checkbox') payload[f.key] = el.checked;
    else if (f.type === 'number') payload[f.key] = el.value === '' ? null : Number(el.value);
    else payload[f.key] = el.value || null;
  });
  return payload;
}

async function submitProduct(e) {
  e.preventDefault();
  const btn = $('#productSubmit');
  const editing = state.editingProductId !== null;
  btn.disabled = true;
  btn.textContent = 'Đang lưu';
  try {
    const payload = collectProductPayload();
    if (editing) {
      await api(`${API.sanPham}/${state.editingProductId}`, { method: 'PUT', body: JSON.stringify(payload) });
    } else {
      await api(API.sanPham, { method: 'POST', body: JSON.stringify(payload) });
    }
    $('#productDialog').close();
    toast(editing ? 'Đã lưu thay đổi' : 'Đã lưu sản phẩm');
    await refreshProducts();
  } catch (err) {
    toast(err.message, 'error');
  } finally {
    btn.disabled = false;
    btn.textContent = editing ? 'Lưu thay đổi' : 'Lưu sản phẩm';
  }
}

async function deleteProduct(p) {
  const ok = await confirmAction({
    title: 'Xóa sản phẩm',
    message: `Xóa “${p.tenSanPham}”? Thao tác này không hoàn tác được.`,
    okLabel: 'Xóa sản phẩm',
  });
  if (!ok) return;
  try {
    await api(`${API.sanPham}/${p.id}`, { method: 'DELETE' });
    toast('Đã xóa sản phẩm');
    await refreshProducts();
  } catch (err) {
    toast(err.message, 'error');
  }
}

/* ---------- Form danh mục ---------- */

function openCategoryForm(category = null) {
  const editing = category !== null;
  state.editingCategoryId = editing ? category.id : null;
  $('#categoryDialogTitle').textContent = editing ? 'Sửa danh mục' : 'Thêm danh mục';
  $('#c_ten').value = editing ? category.tenDanhMuc : '';
  $('#categoryDialog').showModal();
  $('#c_ten').focus();
}

async function submitCategory(e) {
  e.preventDefault();
  const name = $('#c_ten').value.trim();
  if (!name) return;
  const btn = $('#categorySubmit');
  const editing = state.editingCategoryId !== null;
  btn.disabled = true;
  try {
    const body = JSON.stringify({ tenDanhMuc: name });
    if (editing) await api(`${API.danhMuc}/${state.editingCategoryId}`, { method: 'PUT', body });
    else await api(API.danhMuc, { method: 'POST', body });
    $('#categoryDialog').close();
    toast(editing ? 'Đã lưu thay đổi' : 'Đã lưu danh mục');
    await loadCategories();
    await refreshProducts(); // tên danh mục hiển thị trong bảng sản phẩm
  } catch (err) {
    toast(err.message, 'error');
  } finally {
    btn.disabled = false;
  }
}

async function deleteCategory(c) {
  const ok = await confirmAction({
    title: 'Xóa danh mục',
    message: `Xóa danh mục “${c.tenDanhMuc}”? Chỉ xóa được khi danh mục không còn sản phẩm.`,
    okLabel: 'Xóa danh mục',
  });
  if (!ok) return;
  try {
    await api(`${API.danhMuc}/${c.id}`, { method: 'DELETE' });
    toast('Đã xóa danh mục');
    await loadCategories();
  } catch (err) {
    toast(err.message, 'error');
  }
}

/* ---------- Gắn sự kiện ---------- */

function findProduct(id) {
  return state.products.find((p) => p.id === id) || state.alerts.find((p) => p.id === id);
}

function onRowAction(e) {
  const btn = e.target.closest('[data-action]');
  if (!btn) return;
  const id = Number(btn.dataset.id);
  const action = btn.dataset.action;

  if (action === 'edit' || action === 'delete') {
    const p = findProduct(id);
    if (!p) return;
    if (action === 'edit') openProductForm(p); else deleteProduct(p);
  } else {
    const c = state.categories.find((x) => x.id === id);
    if (!c) return;
    if (action === 'edit-cat') openCategoryForm(c); else deleteCategory(c);
  }
}

function bindEvents() {
  $$('.tab').forEach((tab) => tab.addEventListener('click', () => showView(tab.dataset.view)));

  $('#btnAddProduct').addEventListener('click', () => openProductForm());
  $('#btnAddCategory').addEventListener('click', () => openCategoryForm());
  $('#btnReloadAlerts').addEventListener('click', () => loadAlerts().catch((e) => toast(e.message, 'error')));

  $('#filterKeyword').addEventListener('input', (e) => { state.filter.keyword = e.target.value; renderProducts(); });
  $('#filterLoai').addEventListener('change', (e) => { state.filter.loai = e.target.value; renderProducts(); });
  $('#filterDanhMuc').addEventListener('change', async (e) => {
    state.filter.danhMucId = e.target.value;
    try { await loadProducts(); } catch (err) { toast(err.message, 'error'); }
  });

  ['#productRows', '#alertRows', '#categoryRows'].forEach((sel) => $(sel).addEventListener('click', onRowAction));

  // Đổi loại trong form thêm mới thì vẽ lại các trường riêng
  $('#f_loai').addEventListener('change', (e) => renderTypeFields(e.target.value));

  $('#productForm').addEventListener('submit', submitProduct);
  $('#categoryForm').addEventListener('submit', submitCategory);

  // Nút đóng (×, Hủy) trong các form
  $$('dialog [data-close]').forEach((b) => b.addEventListener('click', () => b.closest('dialog').close()));

  // Hộp xác nhận: bấm ra ngoài thì hủy
  $('#confirmDialog').addEventListener('click', (e) => { if (e.target.id === 'confirmDialog') e.target.close('cancel'); });
}

/* ---------- Khởi động ---------- */

async function init() {
  renderLoaiFilter();
  bindEvents();
  try {
    await Promise.all([loadCategories(), loadAlerts()]);
    await loadProducts();
    showBanner('');
  } catch (err) {
    showBanner(err.message);
    renderProducts();
  }
}

document.addEventListener('DOMContentLoaded', init);
