package app.domain.actionTab.options

import app.domain.actionTab.*

data class TabActionOption(
    val name: String,
    val param: Any? = null,
    val action: (NavController, Any?) -> Unit
)
