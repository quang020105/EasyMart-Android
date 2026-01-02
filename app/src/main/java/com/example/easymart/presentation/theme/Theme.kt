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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.example.easymart.presentation.theme.dimens.AppDimens
import com.example.easymart.presentation.theme.dimens.DefaultDimens
import com.example.easymart.presentation.theme.dimens.LargeDimens
import com.example.easymart.presentation.theme.dimens.LocalAppDimens
import com.example.easymart.presentation.theme.type.AppTypography
import com.example.easymart.presentation.theme.type.AppTypographyLarge
import com.example.easymart.presentation.theme.type.Test

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = md_primary,
    secondary = md_secondary,
    tertiary = md_tertiary,
    error = md_error,
    background = md_background,
    surface = md_surface,
    outline = md_outline,
    surfaceVariant = md_surfaceVariant,
    onPrimary = md_onPrimary,
    onSecondary = md_onSecondary,
    onTertiary = md_onTertiary,
    onError = md_onError,
    onBackground = md_onBackground,
    onSurface = md_onSurface,

    primaryContainer = md_primaryContainer,
    onPrimaryContainer = md_onPrimaryContainer,
    secondaryContainer = md_secondaryContainer,
    onSecondaryContainer = md_onSecondaryContainer,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
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
    CompositionLocalProvider(LocalAppDimens provides chosenDimens) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}