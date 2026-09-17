package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val WaynDarkColorScheme = darkColorScheme(
    primary = ElectricOrange,
    onPrimary = MidnightBackground,
    primaryContainer = ElectricOrangeVariant,
    onPrimaryContainer = TextOffWhite,

    secondary = WarmYellow,
    onSecondary = MidnightBackground,
    secondaryContainer = MidnightElevated,
    onSecondaryContainer = WarmYellow,

    tertiary = CoralRed,
    onTertiary = TextOffWhite,
    tertiaryContainer = MidnightElevated,
    onTertiaryContainer = CoralRed,

    background = MidnightBackground,
    onBackground = TextOffWhite,

    surface = MidnightSurface,
    onSurface = TextOffWhite,
    surfaceVariant = MidnightSurfaceVariant,
    onSurfaceVariant = TextMuted,

    error = CoralRed,
    onError = TextOffWhite,

    outline = BorderStroke,
    outlineVariant = BorderStrokeGlow
)

val WaynShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(26.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun WaynTheme(
    content: @Composable () -> Unit
) {
    MyApplicationTheme(content = content)
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    // Dark mode first, intentional custom palette for Wayn? (No generic purple AI aesthetics)
    MaterialTheme(
        colorScheme = WaynDarkColorScheme,
        typography = Typography,
        shapes = WaynShapes,
        content = content
    )
}
