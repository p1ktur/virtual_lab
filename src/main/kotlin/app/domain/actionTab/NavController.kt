package app.domain.actionTab

import moe.tlaster.precompose.navigation.*

typealias Route = String

class NavController(val navigator: Navigator) {

    val canGoBack get() = navigator.canGoBack

    var internalNavigateCallback: ((String) -> Unit)? = null
    var internalGoBackCallback: (() -> Unit)? = null

    val onRouteEnterCallbacks = mutableMapOf<String, () -> Unit>()

    fun navigate(route: Route) {
        internalNavigateCallback?.invoke(route)
        navigator.navigate(route)
    }

    suspend fun navigateForResult(route: Route): Any? {
        internalNavigateCallback?.invoke(route)
        val result = navigator.navigateForResult(route)
        return result
    }

    fun goBack() {
        internalGoBackCallback?.invoke()
        navigator.goBack()
    }

    fun goBackWith(result: Any? = null) {
        internalGoBackCallback?.invoke()
        navigator.goBackWith(result)
    }

    fun clearBackStack(upTo: String = "") {
        navigator.goBack(PopUpTo(upTo))
    }

    fun compareRoutesFirstNodes(route1: Route?, route2: Route?): Boolean {
        if (route1 == null || route2 == null) return false

        return try {
            if (route1.startsWith("/") && route2.startsWith("/")) {
                route1.split("/")[1] == route2.split("/")[1]
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    fun getRouteFirstNode(route: Route): Route {
        return if (route.count { it == '/' } < 2) {
            route
        } else {
            val nodes = route.split("/")
            "/${nodes[1]}"
        }
    }
}