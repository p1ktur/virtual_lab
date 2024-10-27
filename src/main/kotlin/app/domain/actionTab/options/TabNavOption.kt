package app.domain.actionTab.options

data class TabNavOption(
    val name: String,
    val route: String,
    val param: Any? = null
)
