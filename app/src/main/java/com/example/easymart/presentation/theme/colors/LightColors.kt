package com.example.easymart.presentation.theme.colors

import androidx.compose.ui.graphics.Color

// ==============================
// Brand / Primary colors
// Dùng cho:
// - nút chính
// - icon nổi bật
// - logo
// - link quan trọng
// - trạng thái active/focus
// ==============================

// Màu thương hiệu chính của app, dùng cho CTA chính, nút "Lưu", icon quan trọng
val md_primary = Color(0xFF308FFB)

// Màu chữ/icon trên nền primary
val md_onPrimary = Color(0xFFFFFFFF)

// Nền nhẹ hơn của primary, dùng cho chip, badge, card nhấn nhẹ, highlight nhóm
val md_primaryContainer = Color(0xFFD1E4FF)

// Chữ/icon đặt trên primaryContainer
val md_onPrimaryContainer = Color(0xFF00164A)

// Gradient start cho các nút/banner nổi bật nếu cần
val md_primaryGradientStart = Color(0xFF2563EB)

// Gradient end cho các nút/banner nổi bật nếu cần
val md_primaryGradientEnd = Color(0xFF3B82F6)

// Màu viền/focus nhẹ liên quan primary, dùng khi input focus hoặc selected state
val md_focusRing = Color(0xFF93C5FD)

// ==============================
// Secondary / Tertiary colors
// Dùng cho:
// - phần tử phụ
// - nhấn nhá nhẹ
// - phân biệt nhóm nội dung khác
// - tag phụ, chip phụ, icon phụ
// ==============================

// Màu phụ trung tính, dùng cho text/icon phụ hoặc các thành phần phụ trợ
val md_secondary = Color(0xFF535F70)

// Màu chữ/icon trên nền secondary
val md_onSecondary = Color(0xFFFFFFFF)

// Nền nhẹ của secondary, dùng cho chip hoặc highlight phụ
val md_secondaryContainer = Color(0xFFD7E3F8)

// Chữ/icon trên secondaryContainer
val md_onSecondaryContainer = Color(0xFF101C2B)

// Màu nhấn phụ mang tính accent khác primary/secondary
val md_tertiary = Color(0xFF6B5B7D)

// Chữ/icon trên nền tertiary
val md_onTertiary = Color(0xFFFFFFFF)

// ==============================
// Semantic / Status colors
// Dùng cho:
// - validation
// - badge trạng thái
// - snackbar
// - thông báo hệ thống
// - trạng thái upload / warning / error / info
// ==============================

// Màu lỗi, dùng cho text lỗi, viền lỗi, icon lỗi, snackbar lỗi
val md_error = Color(0xFFBA1A1A)

// Chữ/icon trên nền error
val md_onError = Color(0xFFFFFFFF)

// Màu thành công, dùng cho trạng thái save thành công, hoàn tất, valid
val md_success = Color(0xFF00C853)

// Chữ/icon trên nền success
val md_onSuccess = Color(0xFFFFFFFF)

// Màu cảnh báo, dùng cho warning badge, thông báo cần chú ý
val md_warning = Color(0xFFFFAB00)

// Chữ/icon trên nền warning
val md_onWarning = Color(0xFF000000)

// Màu thông tin, dùng cho info badge, hint, trạng thái trung tính có nhấn mạnh
val md_info = Color(0xFF2196F3)

// Chữ/icon trên nền info
val md_onInfo = Color(0xFFFFFFFF)

// Nền xanh nhạt cho thành công, dùng cho chip/banner success nhẹ
val md_successContainer = Color(0xFFDCFCE7)

// Nền vàng nhạt cho cảnh báo, dùng cho chip/banner warning nhẹ
val md_warningContainer = Color(0xFFFEF3C7)

// Nền đỏ nhạt cho lỗi, dùng cho chip/banner error nhẹ
val md_errorContainer = Color(0xFFFEE2E2)

// Nền xanh nhạt cho info, dùng cho chip/banner info nhẹ
val md_infoContainer = Color(0xFFE0F2FE)

// ==============================
// Background / Surface colors
// Dùng cho:
// - nền màn hình
// - card
// - sheet
// - bottom bar
// - vùng chứa nội dung
// ==============================

// Nền tổng của màn hình, thường dùng cho toàn app
val md_background = Color(0xFFF8FAFC)

// Chữ chính trên nền background
val md_onBackground = Color(0xFF1A1C1E)

// Nền card, sheet, bottom bar, vùng chứa chính
val md_surface = Color(0xFFFFFFFF)

// Chữ chính trên surface
val md_onSurface = Color(0xFF1A1C1E)

// Surface variant, dùng cho vùng nền phụ, section nền nhẹ, phân tách khối
val md_surfaceVariant = Color(0xFFDFE2EB)

// Surface sáng hơn, dùng cho panel nổi bật nhẹ
val md_surfaceBright = Color(0xFFFFFFFF)

// Surface tối hơn một chút, dùng cho nền nhấn nhẹ hoặc trạng thái pressed area
val md_surfaceDim = Color(0xFFF3F4F6)

// Nền container thấp, dùng cho khung input, vùng phụ, khối nested
val md_surfaceContainerLow = Color(0xFFF9FAFB)

// Nền container chuẩn, dùng cho card trắng, sheet, block nội dung chính
val md_surfaceContainer = Color(0xFFFFFFFF)

// Nền container cao hơn, dùng cho vùng nested card, grouped card
val md_surfaceContainerHigh = Color(0xFFF3F4F6)

// Nền container cao nhất, dùng cho vùng cần tách mạnh hơn một chút
val md_surfaceContainerHighest = Color(0xFFEFF2F6)

// Màu scrim/overlay cho dialog, bottom sheet, modal
val md_scrim = Color(0x66000000)

// ==============================
// Text / Icon colors
// Dùng cho:
// - tiêu đề
// - nội dung
// - placeholder
// - icon phụ
// - counter
// ==============================

// Text chính, tiêu đề, nội dung quan trọng
val md_textPrimary = Color(0xFF111827)

// Text phụ, subtitle, mô tả phụ
val md_textSecondary = Color(0xFF4B5563)

// Text phụ hơn nữa, dùng cho hint, counter, label mờ
val md_onSurfaceVariant = Color(0xFF6B7280)

// Icon phụ, icon trong input, icon decor, icon muted
val md_iconMuted = Color(0xFF9CA3AF)

// Màu chữ trên nền đã chọn, tag selected, highlight nhẹ
val md_onSelected = Color(0xFF00164A)

// ==============================
// Border / Divider colors
// Dùng cho:
// - viền input
// - card border
// - divider
// - outline nhẹ
// ==============================

// Viền chuẩn, dùng cho card outline hoặc border rõ hơn một chút
val md_outline = Color(0xFF73777F)

// Viền rất nhẹ, dùng cho input border, card outline tinh tế
val md_outlineVariant = Color(0xFFE5E7EB)

// Đường chia giữa các vùng, divider list, separator
val md_divider = Color(0xFFE5E7EB)

// ==============================
// Interaction / State colors
// Dùng cho:
// - pressed state
// - disabled state
// - selected state
// - hover/focus
// ==============================

// Nền khi nhấn nhẹ, dùng cho button/card pressed
val md_pressed = Color(0xFFE0F2FE)

// Nền cho item được chọn
val md_selected = Color(0xFFDCEBFF)

// Nền cho component disabled
val md_disabledContainer = Color(0xFFF3F4F6)

// Chữ/icon cho component disabled
val md_disabledContent = Color(0xFF9CA3AF)

// ==============================
// AI / Special feature colors
// Dùng cho:
// - nút "Quét bằng AI"
// - banner AI
// - feature nổi bật mang tính riêng của EasyMart
// ==============================

// Màu nhấn riêng cho AI feature, giúp phân biệt với primary bình thường
val md_aiAccent = Color(0xFF6366F1)

// Nền nhẹ cho AI card/banner
val md_aiAccentLight = Color(0xFFEEF2FF)

// Màu nhấn phụ cho AI accent nếu cần
val md_aiAccentSoft = Color(0xFFE0E7FF)
