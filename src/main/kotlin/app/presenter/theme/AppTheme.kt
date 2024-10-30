package app.presenter.theme

import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun AppTheme(
    theme: Theme,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = when (theme) {
            Theme.LIGHT -> pinkColorSchemeLight
            Theme.DARK -> pinkColorSchemeDark
        },
        typography = Typography,
        content = content
    )
}