package app.presenter.theme

import androidx.compose.material3.*
import androidx.compose.ui.graphics.*

fun Color.invert(): Color {
    val red = (255 - (red * 255).toInt()).coerceIn(0, 255)
    val green = (255 - (green * 255).toInt()).coerceIn(0, 255)
    val blue = (255 - (blue * 255).toInt()).coerceIn(0, 255)
    val alpha = (alpha * 255).toInt().coerceIn(0, 255)

    return Color(red, green, blue, alpha)
}

val ColorScheme.inverseToPrimary: Color get() = primary.invert()
val ColorScheme.inverseToPrimaryContainer: Color get() = primaryContainer.invert()
val ColorScheme.inverseToSecondary: Color get() = secondary.invert()
val ColorScheme.inverseToSecondaryContainer: Color get() = secondaryContainer.invert()
val ColorScheme.inverseToTertiary: Color get() = tertiary.invert()
val ColorScheme.inverseToTertiaryContainer: Color get() = tertiaryContainer.invert()
val ColorScheme.highlightColor: Color get() = inverseToPrimary.copy(green = 0.55f, blue = 0.95f)

val defaultColorScheme = lightColorScheme()

val pinkColorSchemeLight by lazy {
    defaultColorScheme.copy(
        primary = Color(0xFF7F515F),
        onPrimary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFEBB1C2),
        onPrimaryContainer = Color(0xFF4D2633),
        secondary = Color(0xFF70585E),
        onSecondary = Color(0xFFFFFFFF),
        secondaryContainer = Color(0xFFFBDCE3),
        onSecondaryContainer = Color(0xFF594349),
        tertiary = Color(0xFF7F5537),
        onTertiary = Color(0xFFFFFFFF),
        tertiaryContainer = Color(0xFFEDB592),
        onTertiaryContainer = Color(0xFF4C2910),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFFFFFFF),
        errorContainer = Color(0xFFFFDAD6),
        onErrorContainer = Color(0xFF410002),
        background = Color(0xFFFFF8F8),
        onBackground = Color(0xFF1F1A1B),
        surface = Color(0xFFFFF8F8),
        onSurface = Color(0xFF1F1A1B),
        surfaceVariant = Color(0xFFF1DEE1),
        onSurfaceVariant = Color(0xFF504447),
        outline = Color(0xFF827377),
        inverseSurface = Color(0xFFD4C2C6),
        inverseOnSurface = Color(0xFF342F30),
        inversePrimary = Color(0xFFF9EEEF)
    )
}

val pinkColorSchemeDark by lazy {
    defaultColorScheme.copy(
        primary = Color(0xFFFFD0DC),
        onPrimary = Color(0xFF4B2431),
        primaryContainer = Color(0xFFDCA3B3),
        onPrimaryContainer = Color(0xFF401B28),
        secondary = Color(0xFFDDBFC6),
        onSecondary = Color(0xFF3E2B31),
        secondaryContainer = Color(0xFF4F3A40),
        onSecondaryContainer = Color(0xFFECCDD4),
        tertiary = Color(0xFFFFD3B9),
        onTertiary = Color(0xFF4A280E),
        tertiaryContainer = Color(0xFFDDA784),
        onTertiaryContainer = Color(0xFF3F1F06),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF93000A),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF161213),
        onBackground = Color(0xFFEAE0E1),
        surface = Color(0xFF161213),
        onSurface = Color(0xFFEAE0E1),
        surfaceVariant = Color(0xFF504447),
        onSurfaceVariant = Color(0xFFD4C2C6),
        outline = Color(0xFF9D8D90),
        inverseSurface = Color(0xFF504447),
        inverseOnSurface = Color(0xFFEAE0E1),
        inversePrimary = Color(0xFF342F30)
    )
}