# Hướng dẫn Theme UI cho AI trong EasyMart

Tài liệu này là nguồn hướng dẫn chính để tạo các màn hình UI mới trong EasyMart.

## Quy tắc Theme

Khi tạo màn hình mới hoặc chỉnh sửa màn hình hiện có, luôn phải đọc và tuân theo hệ thống theme dưới đây:

* `AppColors`
* `LightAppColors`
* `DarkAppColors`
* `LightColors`
* `DarkColors`
* `Theme`

Luôn sử dụng hệ thống theme thống nhất để toàn bộ app có giao diện đồng bộ.

---

# Quy tắc sử dụng màu sắc

## 1) Dùng `MaterialTheme.colorScheme` cho màu chuẩn Material 3

Sử dụng `MaterialTheme.colorScheme` cho các token Material 3 chuẩn như:

* `primary`
* `onPrimary`
* `secondary`
* `tertiary`
* `background`
* `surface`
* `onSurface`
* `onBackground`
* `error`
* `outline`
* `outlineVariant`
* `surfaceVariant`
* `onSurfaceVariant`

Các màu này nên được dùng cho:

* text chuẩn
* card
* border
* background
* button
* container
* các component Material 3 tiêu chuẩn

---

## 2) Dùng `LocalAppColors.current` cho màu mở rộng của EasyMart

Sử dụng `LocalAppColors.current` cho các token mở rộng riêng của EasyMart như:

* `surfaceContainerLow`
* `surfaceContainer`
* `surfaceContainerHigh`
* `surfaceContainerHighest`
* `surfaceBright`
* `surfaceDim`
* `iconMuted`
* `textPrimary`
* `textSecondary`
* `pressed`
* `selected`
* `disabledContainer`
* `disabledContent`
* `focusRing`
* `aiAccent`
* `aiAccentLight`
* `aiAccentSoft`
* `successContainer`
* `warningContainer`
* `errorContainer`
* `infoContainer`
* `primaryGradientStart`
* `primaryGradientEnd`

Các token này dùng để giữ UI đồng bộ trên toàn app.

---

# Quy tắc Typography

## Không được hardcode kích thước chữ

Không được viết trực tiếp:

* `14.sp`
* `16.sp`
* `20.sp`

trong screen hoặc component reusable, trừ trường hợp thật sự đặc biệt.

Thay vào đó phải dùng:

* `LocalAppDimens.current.textSmall`
* `LocalAppDimens.current.textBody`
* `LocalAppDimens.current.textTitle`
* `LocalAppDimens.current.textHeading`

Nếu cần thêm size mới:

* thêm vào design system trước
* không hardcode trực tiếp trong UI

---

## Font chữ

Luôn tuân theo typography của Material 3 và typography system của app.

Không override font ngẫu nhiên trong từng màn hình nếu không thật sự cần thiết.

---

# Quy tắc spacing và sizing

Không hardcode spacing khắp UI.

Phải sử dụng `LocalAppDimens.current` cho:

* spacing
* padding
* margin
* corner radius
* icon size
* button height
* card size
* screen padding
* divider thickness

Ví dụ:

* `spaceXs`
* `spaceSm`
* `spaceMd`
* `spaceLg`
* `spaceXl`
* `radiusSmall`
* `radiusMedium`
* `radiusLarge`
* `radiusXl`
* `iconSmall`
* `iconMedium`
* `iconLarge`
* `buttonHeight`
* `screenPadding`

---

# Mục tiêu giao diện

UI phải:

* sạch
* hiện đại
* đồng bộ
* dễ đọc
* premium
* đúng tinh thần Material 3

Ưu tiên:

* bo góc mềm
* border nhẹ
* khoảng trắng hợp lý
* hierarchy rõ ràng
* độ tương phản vừa phải
* component tái sử dụng
* responsive layout

Tránh:

* hardcode màu ngẫu nhiên
* hardcode text size
* mỗi màn hình một kiểu radius
* dùng quá nhiều màu không có hệ thống
* shadow quá nặng
* form style kiểu Android cũ

---

# Checklist khi tạo màn hình mới

Trước khi tạo screen mới, luôn kiểm tra:

1. Màu lấy từ `MaterialTheme.colorScheme` hoặc `LocalAppColors.current`
2. Text size lấy từ `LocalAppDimens.current`
3. Spacing và radius lấy từ `LocalAppDimens.current`
4. Typography tuân theo Material 3
5. Screen đồng bộ với design language của EasyMart
6. UI cân đối, sạch và dễ nhìn

---

# Mapping nhanh

* Nút chính → `primary` / `onPrimary`
* Nút phụ → `secondary` / `onSecondary`
* Nền card → `surface` hoặc `surfaceContainer`
* Background section nhẹ → `surfaceContainerLow`
* Background section mạnh hơn → `surfaceContainerHigh`
* Icon phụ → `iconMuted`
* Hint text → `onSurfaceVariant`
* Border khi focus → `focusRing`
* Selected state → `selected`
* Pressed state → `pressed`
* Feature AI → `aiAccent`
* Success state → `success` / `successContainer`
* Warning state → `warning` / `warningContainer`
* Error state → `error` / `errorContainer`

---

# Quy tắc cuối cùng

Luôn đọc hệ thống theme trước khi generate UI.

Sau đó xây dựng màn hình bằng các design token có sẵn để toàn bộ app giữ được sự đồng bộ về giao diện.
