package com.example.easymart.presentation.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.easymart.presentation.theme.colors.DarkAppColors
import com.example.easymart.presentation.theme.colors.LightAppColors
import com.example.easymart.presentation.theme.colors.LocalAppColors
import com.example.easymart.presentation.theme.colors.Pink80
import com.example.easymart.presentation.theme.colors.Purple80
import com.example.easymart.presentation.theme.colors.PurpleGrey80
import com.example.easymart.presentation.theme.colors.dark_aiAccent
import com.example.easymart.presentation.theme.colors.dark_aiAccentLight
import com.example.easymart.presentation.theme.colors.dark_aiAccentSoft
import com.example.easymart.presentation.theme.colors.dark_background
import com.example.easymart.presentation.theme.colors.dark_disabledContainer
import com.example.easymart.presentation.theme.colors.dark_disabledContent
import com.example.easymart.presentation.theme.colors.dark_divider
import com.example.easymart.presentation.theme.colors.dark_error
import com.example.easymart.presentation.theme.colors.dark_errorContainer
import com.example.easymart.presentation.theme.colors.dark_focusRing
import com.example.easymart.presentation.theme.colors.dark_iconMuted
import com.example.easymart.presentation.theme.colors.dark_info
import com.example.easymart.presentation.theme.colors.dark_infoContainer
import com.example.easymart.presentation.theme.colors.dark_onBackground
import com.example.easymart.presentation.theme.colors.dark_onError
import com.example.easymart.presentation.theme.colors.dark_onInfo
import com.example.easymart.presentation.theme.colors.dark_onPrimary
import com.example.easymart.presentation.theme.colors.dark_onPrimaryContainer
import com.example.easymart.presentation.theme.colors.dark_onSecondary
import com.example.easymart.presentation.theme.colors.dark_onSecondaryContainer
import com.example.easymart.presentation.theme.colors.dark_onSelected
import com.example.easymart.presentation.theme.colors.dark_onSuccess
import com.example.easymart.presentation.theme.colors.dark_onSurface
import com.example.easymart.presentation.theme.colors.dark_onSurfaceVariant
import com.example.easymart.presentation.theme.colors.dark_onTertiary
import com.example.easymart.presentation.theme.colors.dark_onWarning
import com.example.easymart.presentation.theme.colors.dark_outline
import com.example.easymart.presentation.theme.colors.dark_outlineVariant
import com.example.easymart.presentation.theme.colors.dark_pressed
import com.example.easymart.presentation.theme.colors.dark_primary
import com.example.easymart.presentation.theme.colors.dark_primaryContainer
import com.example.easymart.presentation.theme.colors.dark_primaryGradientEnd
import com.example.easymart.presentation.theme.colors.dark_primaryGradientStart
import com.example.easymart.presentation.theme.colors.dark_scrim
import com.example.easymart.presentation.theme.colors.dark_secondary
import com.example.easymart.presentation.theme.colors.dark_secondaryContainer
import com.example.easymart.presentation.theme.colors.dark_selected
import com.example.easymart.presentation.theme.colors.dark_success
import com.example.easymart.presentation.theme.colors.dark_successContainer
import com.example.easymart.presentation.theme.colors.dark_surface
import com.example.easymart.presentation.theme.colors.dark_surfaceBright
import com.example.easymart.presentation.theme.colors.dark_surfaceContainer
import com.example.easymart.presentation.theme.colors.dark_surfaceContainerHigh
import com.example.easymart.presentation.theme.colors.dark_surfaceContainerHighest
import com.example.easymart.presentation.theme.colors.dark_surfaceContainerLow
import com.example.easymart.presentation.theme.colors.dark_surfaceDim
import com.example.easymart.presentation.theme.colors.dark_surfaceVariant
import com.example.easymart.presentation.theme.colors.dark_tertiary
import com.example.easymart.presentation.theme.colors.dark_textPrimary
import com.example.easymart.presentation.theme.colors.dark_textSecondary
import com.example.easymart.presentation.theme.colors.dark_warning
import com.example.easymart.presentation.theme.colors.dark_warningContainer
import com.example.easymart.presentation.theme.colors.md_aiAccent
import com.example.easymart.presentation.theme.colors.md_aiAccentLight
import com.example.easymart.presentation.theme.colors.md_aiAccentSoft
import com.example.easymart.presentation.theme.colors.md_background
import com.example.easymart.presentation.theme.colors.md_disabledContainer
import com.example.easymart.presentation.theme.colors.md_disabledContent
import com.example.easymart.presentation.theme.colors.md_divider
import com.example.easymart.presentation.theme.colors.md_error
import com.example.easymart.presentation.theme.colors.md_errorContainer
import com.example.easymart.presentation.theme.colors.md_focusRing
import com.example.easymart.presentation.theme.colors.md_iconMuted
import com.example.easymart.presentation.theme.colors.md_info
import com.example.easymart.presentation.theme.colors.md_infoContainer
import com.example.easymart.presentation.theme.colors.md_onBackground
import com.example.easymart.presentation.theme.colors.md_onError
import com.example.easymart.presentation.theme.colors.md_onInfo
import com.example.easymart.presentation.theme.colors.md_onPrimary
import com.example.easymart.presentation.theme.colors.md_onPrimaryContainer
import com.example.easymart.presentation.theme.colors.md_onSecondary
import com.example.easymart.presentation.theme.colors.md_onSecondaryContainer
import com.example.easymart.presentation.theme.colors.md_onSelected
import com.example.easymart.presentation.theme.colors.md_onSuccess
import com.example.easymart.presentation.theme.colors.md_onSurface
import com.example.easymart.presentation.theme.colors.md_onSurfaceVariant
import com.example.easymart.presentation.theme.colors.md_onTertiary
import com.example.easymart.presentation.theme.colors.md_onWarning
import com.example.easymart.presentation.theme.colors.md_outline
import com.example.easymart.presentation.theme.colors.md_outlineVariant
import com.example.easymart.presentation.theme.colors.md_pressed
import com.example.easymart.presentation.theme.colors.md_primary
import com.example.easymart.presentation.theme.colors.md_primaryContainer
import com.example.easymart.presentation.theme.colors.md_primaryGradientEnd
import com.example.easymart.presentation.theme.colors.md_primaryGradientStart
import com.example.easymart.presentation.theme.colors.md_scrim
import com.example.easymart.presentation.theme.colors.md_secondary
import com.example.easymart.presentation.theme.colors.md_secondaryContainer
import com.example.easymart.presentation.theme.colors.md_selected
import com.example.easymart.presentation.theme.colors.md_success
import com.example.easymart.presentation.theme.colors.md_successContainer
import com.example.easymart.presentation.theme.colors.md_surface
import com.example.easymart.presentation.theme.colors.md_surfaceBright
import com.example.easymart.presentation.theme.colors.md_surfaceContainer
import com.example.easymart.presentation.theme.colors.md_surfaceContainerHigh
import com.example.easymart.presentation.theme.colors.md_surfaceContainerHighest
import com.example.easymart.presentation.theme.colors.md_surfaceContainerLow
import com.example.easymart.presentation.theme.colors.md_surfaceDim
import com.example.easymart.presentation.theme.colors.md_surfaceVariant
import com.example.easymart.presentation.theme.colors.md_tertiary
import com.example.easymart.presentation.theme.colors.md_textPrimary
import com.example.easymart.presentation.theme.colors.md_textSecondary
import com.example.easymart.presentation.theme.colors.md_warning
import com.example.easymart.presentation.theme.colors.md_warningContainer
import com.example.easymart.presentation.theme.dimens.AppDimens
import com.example.easymart.presentation.theme.dimens.DefaultDimens
import com.example.easymart.presentation.theme.dimens.LargeDimens
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.theme.type.AppTypography
import com.example.easymart.presentation.theme.type.AppTypographyLarge



//private val LightColorScheme = lightColorScheme(
//    primary = md_primary,
//    secondary = md_secondary,
//    tertiary = md_tertiary,
//    error = md_error,
//    background = md_background,
//    surface = md_surface,
//    outline = md_outline,
//    surfaceVariant = md_surfaceVariant,
//    onPrimary = md_onPrimary,
//    onSecondary = md_onSecondary,
//    onTertiary = md_onTertiary,
//    onError = md_onError,
//    onBackground = md_onBackground,
//    onSurface = md_onSurface,
//
//    primaryContainer = md_primaryContainer,
//    onPrimaryContainer = md_onPrimaryContainer,
//    secondaryContainer = md_secondaryContainer,
//    onSecondaryContainer = md_onSecondaryContainer,
//
//    /* Other default colors to override
//    background = Color(0xFFFFFBFE),
//    surface = Color(0xFFFFFBFE),
//    onPrimary = Color.White,
//    onSecondary = Color.White,
//    onTertiary = Color.White,
//    onBackground = Color(0xFF1C1B1F),
//    onSurface = Color(0xFF1C1B1F),
//    */
//)



private val LightColorScheme = lightColorScheme(
    primary = md_primary,
    onPrimary = md_onPrimary,
    primaryContainer = md_primaryContainer,
    onPrimaryContainer = md_onPrimaryContainer,


    secondary = md_secondary,
    onSecondary = md_onSecondary,
    secondaryContainer = md_secondaryContainer,
    onSecondaryContainer = md_onSecondaryContainer,

    tertiary = md_tertiary,
    onTertiary = md_onTertiary,

    error = md_error,
    onError = md_onError,
    errorContainer = md_errorContainer,

    background = md_background,
    onBackground = md_onBackground,

    surface = md_surface,
    onSurface = md_onSurface,
    surfaceVariant = md_surfaceVariant,
    onSurfaceVariant = md_onSurfaceVariant,

    outline = md_outline,
    outlineVariant = md_outlineVariant,

    scrim = md_scrim,
    surfaceTint = md_primary,

    // Các màu tiện ích cho UI custom trong app
    // Không phải lúc nào cũng dùng trực tiếp qua MaterialTheme.colorScheme,
    // nhưng vẫn để ở đây để theme đồng bộ.
    inversePrimary = md_primary,
)


private val DarkColorScheme = darkColorScheme(
    primary = dark_primary,
    onPrimary = dark_onPrimary,
    primaryContainer = dark_primaryContainer,
    onPrimaryContainer = dark_onPrimaryContainer,

    secondary = dark_secondary,
    onSecondary = dark_onSecondary,
    secondaryContainer = dark_secondaryContainer,
    onSecondaryContainer = dark_onSecondaryContainer,

    tertiary = dark_tertiary,
    onTertiary = dark_onTertiary,

    error = dark_error,
    onError = dark_onError,
    errorContainer = dark_errorContainer,

    background = dark_background,
    onBackground = dark_onBackground,

    surface = dark_surface,
    onSurface = dark_onSurface,
    surfaceVariant = dark_surfaceVariant,
    onSurfaceVariant = dark_onSurfaceVariant,

    outline = dark_outline,
    outlineVariant = dark_outlineVariant,

    scrim = dark_scrim,
    surfaceTint = dark_primary,

    inversePrimary = dark_primary
)

//Ngưỡng để chọn dimens phù hợp với kích thước (phone hoặc tablet)
private const val DIMENS_THRESHOLD = 600

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun EasyMartTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    forceDimens: AppDimens? = null,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // cấu hình màu sắc tùy chỉnh cho app,  cung cấp thêm màu ngoài colorScheme nếu cần
    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    //lấy cấu hình màn hiện tại
    val configuration = LocalConfiguration.current
    //chiều rộng màn hình
    val screenWidth = configuration.screenWidthDp
    // Nếu caller cung cấp forceDimens, dùng nó; nếu không, chọn theo screenWidthDp
    val chosenDimens = forceDimens ?: run {
        if(screenWidth < DIMENS_THRESHOLD) DefaultDimens else LargeDimens
    }
    //chọn kiểu chữ dựa trên kích thước màn hình
    val typography = if(screenWidth < DIMENS_THRESHOLD) AppTypography else AppTypographyLarge

    // Compose provider để cung cấp LocalAppDimens cho cả app
    CompositionLocalProvider(
        LocalAppDimens provides chosenDimens,
        LocalAppColors provides appColors){
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}