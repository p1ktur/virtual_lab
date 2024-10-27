package app.domain.actionTab.options

data class TabActionOption(
    val name: String,
    val param: Any? = null,
    val action: (Any?) -> Unit
)
