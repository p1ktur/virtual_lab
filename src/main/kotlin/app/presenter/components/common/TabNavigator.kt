package app.presenter.components.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import app.domain.actionTab.*
import app.domain.actionTab.options.*
import app.presenter.theme.*
import moe.tlaster.precompose.navigation.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TabNavigator(
    navOptions: List<TabNavOption>,
    actionOptions: List<TabActionOption>,
    menuOptions: List<MenuOption> = emptyList(),
    isLoading: Boolean = false,
    navigationAllowed: Boolean = true,
    onNavOptionClick: ((TabNavOption) -> Unit)? = null,
    onLogOut: (() -> Unit)? = null,
    content: @Composable BoxScope.(NavController) -> Unit
) {
    val navigationRouteStack = remember { mutableStateListOf<String>() }
    var currentRoute by remember { mutableStateOf<String?>(null) }

    val navigator = rememberNavigator()
    val navController by remember(navigator) {
        val navController = NavController(navigator).apply {
            internalNavigateCallback = { route ->
                navigationRouteStack.add(route)
                currentRoute = route

                currentRoute?.let {
                    onRouteEnterCallbacks[getRouteFirstNode(it)]?.invoke()
                }
            }
            internalGoBackCallback = {
                if (navigationRouteStack.isNotEmpty()) navigationRouteStack.removeLast()
                if (navigationRouteStack.isNotEmpty()) currentRoute = navigationRouteStack.last()

                currentRoute?.let {
                    onRouteEnterCallbacks[getRouteFirstNode(it)]?.invoke()
                }
            }
        }

        mutableStateOf(navController)
    }
    val canNavControllerGoBack by navController.canGoBack.collectAsState(false)
    val currentEntry by navigator.currentEntry.collectAsState(null)

    var isMenuExpanded by remember { mutableStateOf(false) }
    val localMenuOptions = remember(menuOptions, onLogOut, currentEntry) {
        val list = menuOptions.filter {
            it.associatedRoutes.any { route ->
                navController.compareRoutesFirstNodes(route, currentEntry?.route?.route)
            }
        }.toMutableStateList()

//        if (onLogOut != null) list.add(
//            MenuOption(
//                text = "Log out",
//                enabled = mutableStateOf(true),
//                onClick = {
//                    onLogOut.invoke()
//                    navController.clearBackStack()
//
//                    if (navigationRouteStack.size > 1) navigationRouteStack.removeRange(1, navigationRouteStack.size)
//                }
//            )
//        )
        list
    }

    LaunchedEffect(Unit) {
        if (navOptions.isNotEmpty()) {
            navigationRouteStack.add(navOptions[0].route)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(LocalAppTheme.current.screenOne)
        ) {
            val rowScrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rowScrollState),
                verticalAlignment = Alignment.CenterVertically
            ) {
                navOptions.forEachIndexed { index, tabNavOption ->
                    val firstCheck = remember(currentRoute, currentEntry) {
                        navController.compareRoutesFirstNodes(currentRoute, tabNavOption.route)
                    }
                    val secondCheck = remember(currentRoute, currentEntry) {
                        navOptions.any { option ->
                            currentEntry?.route?.route?.let {
                                navController.compareRoutesFirstNodes(it, option.route)
                            } ?: false
                        }
                    }

                    Text(
                        modifier = Modifier
                            .clickable {
                                if (navigationAllowed && !navController.compareRoutesFirstNodes(currentRoute, tabNavOption.route)) {
                                    currentRoute = tabNavOption.route
                                    navController.navigate(tabNavOption.route)
                                    onNavOptionClick?.invoke(tabNavOption)
                                }
                            }
                            .then(
                                if (firstCheck && secondCheck) {
                                    Modifier.background(LocalAppTheme.current.screenOneDimmed)
                                } else {
                                    Modifier
                                }
                            )
                            .padding(8.dp),
                        text = tabNavOption.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (firstCheck && secondCheck) {
                            LocalAppTheme.current.text
                        } else {
                            LocalAppTheme.current.textInverse
                        }
                    )
                    if (index != navOptions.size - 1) {
                        VerticalDivider(
                            color = LocalAppTheme.current.divider,
                            fillMaxHeight = 1f,
                            verticalPadding = 4.dp
                        )
                    }
                }
                if (navOptions.isNotEmpty() && actionOptions.isNotEmpty() && (rowScrollState.canScrollForward || rowScrollState.canScrollBackward)) {
                    Spacer(modifier = Modifier.width(64.dp))
                }
                Spacer(modifier = Modifier.weight(1f))
                actionOptions.forEachIndexed { index, tabActionOption ->
                    Text(
                        modifier = Modifier
                            .clickable {
                                tabActionOption.action(navController, tabActionOption.param)
                            }
                            .padding(8.dp),
                        text = tabActionOption.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = LocalAppTheme.current.text
                    )
                    if (index != actionOptions.size - 1 || localMenuOptions.isNotEmpty()) {
                        VerticalDivider(
                            color = LocalAppTheme.current.divider,
                            fillMaxHeight = 1f,
                            verticalPadding = 4.dp
                        )
                    }
                }
            }
            if (localMenuOptions.isNotEmpty()) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            isMenuExpanded = true
                        }
                        .padding(8.dp),
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = LocalAppTheme.current.text
                )
            }
        }
        HorizontalDivider(
            fillMaxWidth = 1f,
            color = LocalAppTheme.current.divider
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                content(navController)
                if (isLoading) CircularProgressIndicator()
            }
            if (canNavControllerGoBack) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.TopStart)
                        .clickable {
                            navController.goBack()
                        }
                        .padding(8.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = LocalAppTheme.current.text
                )
            }
            if (localMenuOptions.isNotEmpty()) {
                ExposedDropdownMenuBox(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .width(108.dp),
                    expanded = isMenuExpanded,
                    onExpandedChange = { newValue ->
                        isMenuExpanded = newValue
                    }
                ) {
                    ExposedDropdownMenu(
                        modifier = Modifier
                            .background(LocalAppTheme.current.screenTwo)
                            .border(1.dp, LocalAppTheme.current.divider),
                        expanded = isMenuExpanded,
                        onDismissRequest = {
                            isMenuExpanded = false
                        }
                    ) {
                        localMenuOptions.forEach { option ->
                            DropdownMenuItem(
                                modifier = Modifier.height(24.dp),
                                enabled = option.enabled.value,
                                onClick = {
                                    if (navigationAllowed) {
                                        option.onClick()
                                        isMenuExpanded = false
                                    }
                                }
                            ) {
                                Text(
                                    text = option.text,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (option.enabled.value) {
                                        LocalAppTheme.current.text
                                    } else {
                                        Color.LightGray
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}