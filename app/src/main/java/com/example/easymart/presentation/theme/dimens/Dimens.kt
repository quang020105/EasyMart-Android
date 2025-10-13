package com.example.easymart.presentation.theme.dimens

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Immutable
data class AppDimens(
    // Dùng cho khoảng cách giữa các phần tử
    val spaceXs: Dp, // Dùng cho padding nhỏ, ví dụ giữa icon và text
    val spaceSm: Dp, // Padding vừa phải giữa các button
    val spaceMd: Dp, // Khoảng cách giữa card hoặc section
    val spaceLg: Dp, // Padding màn hình
    val spaceXl: Dp,  // Margin lớn ở màn hình chính
    val space2xl: Dp,  // Khoảng cách  lớn
    val space3xl: Dp,  // Khoảng cách cực lớn
    val space4xl: Dp,  // Khoảng cách cực lớn

    // ---------- RADIUS ----------
    // Dùng cho bo góc các thành phần UI
    val radiusSmall: Dp,  // bo góc cho chip hoặc tag
    val radiusMedium: Dp, // bo góc cho button, card nhỏ
    val radiusLarge: Dp,  // bo góc cho card
    val radiusXl: Dp,  // bo góc cho card lớn hoặc container

    // ---------- ICON & SIZE ----------
    val iconSmall: Dp,   // icon trong TextField, toolbar
    val iconMedium: Dp,  // icon trong button, list item
    val iconLarge: Dp,   // icon ở banner, splash
    val buttonHeight: Dp,// chiều cao mặc định của button

    // ---------- TEXT ----------
    val textSmall: TextUnit, // text phụ, mô tả
    val textBody: TextUnit,  // nội dung chính
    val textTitle: TextUnit, // tiêu đề lớn
    val textHeading: TextUnit, // heading / appbar title

    // ---------- LAYOUT ----------
    val screenPadding: Dp,  // padding 2 bên của màn hình
    val cardElevation: Dp,  // độ nổi của card
    val dividerThickness: Dp, // độ dày của Divider

    val featuredCardHeight: Dp,
    val recommendedHeight: Dp,
    val recommendedWidth: Dp
)

val LocalAppDimens = staticCompositionLocalOf { DefaultDimens }