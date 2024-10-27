package app.presenter.components.common

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material.ripple.*
import androidx.compose.material3.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import app.domain.actionTab.*
import app.domain.actionTab.options.*
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
            navigateCallback = { route ->
                navigationRouteStack.add(route)
                currentRoute = route
            }
            goBackCallback = {
                if (navigationRouteStack.isNotEmpty()) navigationRouteStack.removeLast()
                if (navigationRouteStack.isNotEmpty()) currentRoute = navigationRouteStack.last()
            }
        }

        mutableStateOf(navController)
    }
    val canNavControllerGoBack by navController.canGoBack.collectAsState(false)
    val currentEntry by navigator.currentEntry.collectAsState(null)

    var isMenuExpanded by remember { mutableStateOf(false) }
    val localMenuOptions = remember(menuOptions, onLogOut, currentEntry) {
        val list = menuOptions.filter { it.associatedRoutes.contains(currentEntry?.route?.route) }.toMutableStateList()

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
                .background(MaterialTheme.colorScheme.primary)
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
                        navController.compareRoutes(currentRoute, tabNavOption.route)
                    }
                    val secondCheck = remember(currentRoute, currentEntry) {
                        navOptions.any { option ->
                            currentEntry?.route?.route?.let {
                                navController.compareRoutes(it, option.route)
                            } ?: false
                        }
                    }

                    Text(
                        modifier = Modifier
                            .clickable(onClick = {
                                if (navigationAllowed && !navController.compareRoutes(currentRoute, tabNavOption.route)) {
                                    currentRoute = tabNavOption.route
                                    navController.navigate(tabNavOption.route)
                                    onNavOptionClick?.invoke(tabNavOption)
                                }
                            })
                            .then(
                                if (firstCheck && secondCheck) {
                                    Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                                } else {
                                    Modifier
                                }
                            )
                            .padding(8.dp),
                        text = tabNavOption.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (firstCheck && secondCheck) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    )
                    if (index != navOptions.size - 1) {
                        VerticalDivider(
                            color = MaterialTheme.colorScheme.onPrimary,
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
                    val interactionSource = remember { MutableInteractionSource() }
                    val isPressed by interactionSource.collectIsPressedAsState()

                    Text(
                        modifier = Modifier
                            .clickable(
                                interactionSource = interactionSource,
                                indication = rememberRipple(),
                                onClick = {
                                    tabActionOption.action(tabActionOption.param)
                                }
                            )
                            .then(
                                if (isPressed) {
                                    Modifier.background(MaterialTheme.colorScheme.primaryContainer)
                                } else {
                                    Modifier
                                }
                            )
                            .padding(8.dp),
                        text = tabActionOption.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isPressed) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onPrimary
                        }
                    )
                    if (index != actionOptions.size - 1 || localMenuOptions.isNotEmpty()) {
                        VerticalDivider(
                            color = MaterialTheme.colorScheme.onPrimary,
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
                        .clickable(onClick = {
                            isMenuExpanded = true
                        })
                        .padding(8.dp),
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
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
                        .clickable(onClick = {
                            navController.goBack()
                        })
                        .padding(8.dp),
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = MaterialTheme.colorScheme.onBackground
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
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .border(2.dp, MaterialTheme.colorScheme.primary),
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
                                        MaterialTheme.colorScheme.onSecondaryContainer
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