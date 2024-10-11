package app.presenter.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import app.presenter.components.common.*
import app.presenter.screens.designing.*
import moe.tlaster.precompose.navigation.*

@Composable
fun NavigationScreen() {
    TabNavigator(
        navOptions = listOf(
//            TabNavOption(
//                name = "Welcome",
//                route = "/welcome"
//            ),
        ),
        menuOptions = listOf(),
        isLoading = false,
        navigationAllowed = true
    ) { navController ->
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navigator = navController.navigator,
            initialRoute = "/designing"
        ) {
            scene(route = "/designing") {
                DesigningScreen()
            }
        }
    }
}