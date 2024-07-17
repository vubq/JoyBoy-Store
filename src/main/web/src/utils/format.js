export function formatCurrencyVND(value) {
  return value.toLocaleString('vi-VN', { style: 'currency', currency: 'VND' })
}
