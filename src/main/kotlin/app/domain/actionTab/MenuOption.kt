package app.domain.actionTab

import androidx.compose.runtime.*

data class MenuOption(
    val text: String,
    var enabled: State<Boolean>,
    val associatedRoutes: List<String> = emptyList(),
    val onClick: () -> Unit
)