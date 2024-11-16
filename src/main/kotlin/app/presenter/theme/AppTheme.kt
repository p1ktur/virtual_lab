package app.presenter.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*

enum class Theme {
    LIGHT,
    DARK;

    operator fun not(): Theme {
        return when(this) {
            LIGHT -> DARK
            DARK -> LIGHT
        }
    }
}

class AppTheme(val theme: Theme) {
    val screenZero: Color
        get() = when (theme) {
            Theme.LIGHT -> screenZeroLight
            Theme.DARK -> screenZeroDark
        }
    val screenOne: Color
        get() = when (theme) {
            Theme.LIGHT -> screenOneLight
            Theme.DARK -> screenOneDark
        }
    val screenOneDimmed: Color
        get() = when (theme) {
            Theme.LIGHT -> screenOneDimmedLight
            Theme.DARK -> screenOneDimmedDark
        }
    val screenTwo: Color
        get() = when (theme) {
            Theme.LIGHT -> screenTwoLight
            Theme.DARK -> screenTwoDark
        }
    val screenThree: Color
        get() = when (theme) {
            Theme.LIGHT -> screenThreeLight
            Theme.DARK -> screenThreeDark
        }
    val background: Color
        get() = when (theme) {
            Theme.LIGHT -> backgroundLight
            Theme.DARK -> backgroundDark
        }
    val divider: Color
        get() = when (theme) {
            Theme.LIGHT -> dividerLight
            Theme.DARK -> dividerDark
        }
    val text: Color
        get() = when (theme) {
            Theme.LIGHT -> textLight
            Theme.DARK -> textDark
        }
    val textInverse: Color
        get() = when (theme) {
            Theme.LIGHT -> textInverseLight
            Theme.DARK -> textInverseDark
        }
    val textDimmed: Color
        get() = when (theme) {
            Theme.LIGHT -> textDimmedLight
            Theme.DARK -> textDimmedDark
        }
    val textDimmedInverse: Color
        get() = when (theme) {
            Theme.LIGHT -> textDimmedInverseLight
            Theme.DARK -> textDimmedInverseDark
        }
    val container: Color
        get() = when (theme) {
            Theme.LIGHT -> containerLight
            Theme.DARK -> containerDark
        }
    val highlightColor: Color
        get() = when (theme) {
            Theme.LIGHT -> highlightColorLight
            Theme.DARK -> highlightColorDark
        }
    val canvasBackground: Color
        get() = when (theme) {
            Theme.LIGHT -> canvasBackgroundLight
            Theme.DARK -> canvasBackgroundDark
        }
    val canvasGrid: Color
        get() = when (theme) {
            Theme.LIGHT -> canvasGridLight
            Theme.DARK -> canvasGridDark
        }
}

val LocalAppTheme = staticCompositionLocalOf { AppTheme(Theme.LIGHT) }

@Composable
fun AppTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = Typography
    ) {
        val appTheme = AppTheme(theme)

        CompositionLocalProvider(LocalAppTheme provides appTheme) {
            content()
        }
    }
}