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
    val primaryScreenZero: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenZeroLight
            Theme.DARK -> primaryScreenZeroDark
        }
    val primaryScreenOne: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenOneLight
            Theme.DARK -> primaryScreenOneDark
        }
    val primaryScreenOneDimmed: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenOneDimmedLight
            Theme.DARK -> primaryScreenOneDimmedDark
        }
    val primaryScreenTwo: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenTwoLight
            Theme.DARK -> primaryScreenTwoDark
        }
    val primaryScreenThree: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenThreeLight
            Theme.DARK -> primaryScreenThreeDark
        }
    val primaryScreenBackground: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenBackgroundLight
            Theme.DARK -> primaryScreenBackgroundDark
        }
    val primaryScreenDivider: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenDividerLight
            Theme.DARK -> primaryScreenDividerDark
        }
    val primaryScreenText: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenTextLight
            Theme.DARK -> primaryScreenTextDark
        }
    val primaryScreenTextInverse: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenTextInverseLight
            Theme.DARK -> primaryScreenTextInverseDark
        }
    val primaryScreenTextDimmed: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenTextDimmedLight
            Theme.DARK -> primaryScreenTextDimmedDark
        }
    val primaryScreenTextDimmedInverse: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenTextDimmedInverseLight
            Theme.DARK -> primaryScreenTextDimmedInverseDark
        }
    val primaryScreenTextContainer: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenTextContainerLight
            Theme.DARK -> primaryScreenTextContainerDark
        }
    val primaryScreenHighlightColor: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryScreenHighlightColorLight
            Theme.DARK -> primaryScreenHighlightColorDark
        }
    val primaryCanvasBackground: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryCanvasBackgroundLight
            Theme.DARK -> primaryCanvasBackgroundDark
        }
    val primaryCanvasGrid: Color
        get() = when (theme) {
            Theme.LIGHT -> primaryCanvasGridLight
            Theme.DARK -> primaryCanvasGridDark
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