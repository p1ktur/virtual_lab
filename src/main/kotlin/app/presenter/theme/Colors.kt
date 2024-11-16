package app.presenter.theme

import androidx.compose.ui.graphics.*

// Own Theme 0 1 2 3 4 5 6 7 8 9 A B C D E F

val screenZeroLight = Color(0xFFFFDFEF) // 0xFFD8A8B8
val screenOneLight = Color(0xFFFFDFEB) // 0xFFE0B0C0
val screenOneDimmedLight = screenOneLight.run { copy(1f, red * 0.55f, green * 0.55f, blue * 0.55f) }
val screenTwoLight = Color(0xFFF8DBE7) // 0xFFE8B8C8
val screenThreeLight = Color(0xFFEFD7E3) // 0xFFF0C0D0
val backgroundLight = Color(0xFFFFF8F8)
val dividerLight = Color(0xFF4D2633)
val textLight = Color(0xFF4D2633)
val textInverseLight = Color(0xFFEFD7E3)
val textDimmedLight = Color(0xFF4D2633)
val textDimmedInverseLight = Color(0xFF7F515F)
val containerLight = Color(0xFFFDEAED) // 0xFFFBDCE3
val highlightColorLight = Color(0xFF7788DD)
val canvasBackgroundLight = Color(0xFFF8F0F0)
val canvasGridLight = Color(0xFF997788)

val screenZeroDark = Color(0xFF6F4F5F)
val screenOneDark = Color(0xFF684858)
val screenOneDimmedDark = screenOneDark.run { copy(1f, red * 0.55f, green * 0.55f, blue * 0.55f) }
val screenTwoDark = Color(0xFF5F3F4F)
val screenThreeDark = Color(0xFF583848)
val backgroundDark = Color(0xFF080000)
val dividerDark = Color(0xFF7F515F)
val textDark = Color(0xFFEFD7E3)
val textInverseDark = Color(0xFF4D2633)
val textDimmedDark = Color(0xFF7F515F)
val textDimmedInverseDark = Color(0xFF4D2633)
val containerDark = Color(0xFF876276)
val highlightColorDark = Color(0xFF7788DD)
val canvasBackgroundDark = Color(0xFF997788)
val canvasGridDark = Color(0xFF4D2633)

// Material 3 DEPRECATED

//fun Color.invert(): Color {
//    val red = (255 - (red * 255).toInt()).coerceIn(0, 255)
//    val green = (255 - (green * 255).toInt()).coerceIn(0, 255)
//    val blue = (255 - (blue * 255).toInt()).coerceIn(0, 255)
//    val alpha = (alpha * 255).toInt().coerceIn(0, 255)
//
//    return Color(red, green, blue, alpha)
//}

//val ColorScheme.inverseToPrimary: Color get() = primary.invert()
//val ColorScheme.inverseToPrimaryContainer: Color get() = primaryContainer.invert()
//val ColorScheme.inverseToSecondary: Color get() = secondary.invert()
//val ColorScheme.inverseToSecondaryContainer: Color get() = secondaryContainer.invert()
//val ColorScheme.inverseToTertiary: Color get() = tertiary.invert()
//val ColorScheme.inverseToTertiaryContainer: Color get() = tertiaryContainer.invert()
//val ColorScheme.highlightColor: Color get() = inverseToPrimary.copy(green = 0.55f, blue = 0.95f)

//val defaultColorScheme = lightColorScheme()

//val pinkColorSchemeLight by lazy {
//    defaultColorScheme.copy(
//        primary = Color(0xFF7F515F),
//        onPrimary = Color(0xFFFFFFFF),
//        primaryContainer = Color(0xFFEBB1C2),
//        onPrimaryContainer = Color(0xFF4D2633),
//        secondary = Color(0xFF70585E),
//        onSecondary = Color(0xFFFFFFFF),
//        secondaryContainer = Color(0xFFFBDCE3),
//        onSecondaryContainer = Color(0xFF594349),
//        tertiary = Color(0xFF7F5537),
//        onTertiary = Color(0xFFFFFFFF),
//        tertiaryContainer = Color(0xFFEDB592),
//        onTertiaryContainer = Color(0xFF4C2910),
//        error = Color(0xFFBA1A1A),
//        onError = Color(0xFFFFFFFF),
//        errorContainer = Color(0xFFFFDAD6),
//        onErrorContainer = Color(0xFF410002),
//        background = Color(0xFFFFF8F8),
//        onBackground = Color(0xFF1F1A1B),
//        surface = Color(0xFFFFF8F8),
//        onSurface = Color(0xFF1F1A1B),
//        surfaceVariant = Color(0xFFF1DEE1),
//        onSurfaceVariant = Color(0xFF504447),
//        outline = Color(0xFF827377),
//        inverseSurface = Color(0xFFD4C2C6),
//        inverseOnSurface = Color(0xFF342F30),
//        inversePrimary = Color(0xFFF9EEEF)
//    )
//}
//
//val pinkColorSchemeDark by lazy {
//    defaultColorScheme.copy(
//        primary = Color(0xFFFFD0DC),
//        onPrimary = Color(0xFF4B2431),
//        primaryContainer = Color(0xFFDCA3B3),
//        onPrimaryContainer = Color(0xFF401B28),
//        secondary = Color(0xFFDDBFC6),
//        onSecondary = Color(0xFF3E2B31),
//        secondaryContainer = Color(0xFF4F3A40),
//        onSecondaryContainer = Color(0xFFECCDD4),
//        tertiary = Color(0xFFFFD3B9),
//        onTertiary = Color(0xFF4A280E),
//        tertiaryContainer = Color(0xFFDDA784),
//        onTertiaryContainer = Color(0xFF3F1F06),
//        error = Color(0xFFFFB4AB),
//        onError = Color(0xFF690005),
//        errorContainer = Color(0xFF93000A),
//        onErrorContainer = Color(0xFFFFDAD6),
//        background = Color(0xFF161213),
//        onBackground = Color(0xFFEAE0E1),
//        surface = Color(0xFF161213),
//        onSurface = Color(0xFFEAE0E1),
//        surfaceVariant = Color(0xFF504447),
//        onSurfaceVariant = Color(0xFFD4C2C6),
//        outline = Color(0xFF9D8D90),
//        inverseSurface = Color(0xFF504447),
//        inverseOnSurface = Color(0xFFEAE0E1),
//        inversePrimary = Color(0xFF342F30)
//    )
//}