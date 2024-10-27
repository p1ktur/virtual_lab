package app.presenter.theme

import androidx.compose.material3.*
import androidx.compose.ui.graphics.*

// Values

val highlightColor = Color(0xFFEB9E34)

// Schemes

val defaultColorScheme = lightColorScheme()

val blueColorSchemeLight by lazy {
    defaultColorScheme.copy(
        primary = Color(0xFFAFAFFF),
        onPrimary = Color(0xFF444488),
        primaryContainer = Color(0xFFB8B8FF),
        onPrimaryContainer = Color(0xFF555599),
        secondary = Color(0xFFBFBFFF),
        onSecondary = Color(0xFF6666AA),
        secondaryContainer = Color(0xFFC8C8FF),
        onSecondaryContainer = Color(0xFF7777BB),
        tertiary = Color(0xFFCFCFFF),
        onTertiary = Color(0xFF8888CC),
        tertiaryContainer = Color(0xFFD8D8FF),
        onTertiaryContainer = Color(0xFF9999DD),
        error = Color(0xFFFFB4AB),
        onError = Color(0xFF690005),
        errorContainer = Color(0xFF220041),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFFF0F0FF),
        onBackground = Color(0xFF202040),
        surface = Color(0xFFF7F9FF),
        onSurface = Color(0xFF181C20),
        surfaceVariant = Color(0xFFDDE3EA),
        onSurfaceVariant = Color(0xFF41474D),
        outline = Color(0xFF72787E),
        inverseSurface = Color(0xFF2D3135),
        inverseOnSurface = Color(0xFFEEF1F6),
        inversePrimary = Color(0xFF95CDF8)
    )
}

val blueColorSchemeDark by lazy {
    defaultColorScheme.copy(
        primary = Color(0xFF444488),
        onPrimary = Color(0xFFAFAFFF),
        primaryContainer = Color(0xFF555599),
        onPrimaryContainer = Color(0xFFB8B8FF),
        secondary = Color(0xFF6666AA),
        onSecondary = Color(0xFFBFBFFF),
        secondaryContainer = Color(0xFF7777BB),
        onSecondaryContainer = Color(0xFFC8C8FF),
        tertiary = Color(0xFF8888CC),
        onTertiary = Color(0xFFCFCFFF),
        tertiaryContainer = Color(0xFF9999DD),
        onTertiaryContainer = Color(0xFFD8D8FF),
        error = Color(0xFFBA1A1A),
        onError = Color(0xFFDDDDFF),
        errorContainer = Color(0xFFFFD6EE),
        onErrorContainer = Color(0xFF220041),
        background = Color(0xFF202040),
        onBackground = Color(0xFFF0F0FF),
        surface = Color(0xFF101417),
        onSurface = Color(0xFFE0E3E8),
        surfaceVariant = Color(0xFF41474D),
        onSurfaceVariant = Color(0xFFC1C7CE),
        outline = Color(0xFF8B9198),
        inverseSurface = Color(0xFFE0E3E8),
        inverseOnSurface = Color(0xFF2D3135),
        inversePrimary = Color(0xFF276389)
    )
}