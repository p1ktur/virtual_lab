package app.domain.tabNavigator

import androidx.compose.runtime.*

data class MenuOption(
    val text: String,
    var enabled: State<Boolean>,
    val showOnlyOnWelcomeScreen: Boolean = false,
    val onClick: () -> Unit
)