package ru.sfu.electro98.shikimori_mobile.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
        primary = PrimaryColor,
        primaryVariant = PrimaryColor,
        secondary = FormSurfaceColor,
        secondaryVariant = PlannedButtonLine,
        surface = HeaderSurfaceColor,
)

private val LightColorPalette = lightColors(
        primary = PrimaryColor,
        primaryVariant = PrimaryColor,
        secondary = FormSurfaceColor,
        secondaryVariant = PlannedButtonLine,
        surface = HeaderSurfaceColor,
)

@Composable
fun ShikimoriMobileTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
    )
}