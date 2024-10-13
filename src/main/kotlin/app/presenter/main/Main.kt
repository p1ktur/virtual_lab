package app.presenter.main

import androidx.compose.foundation.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import app.domain.di.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import app.presenter.components.window.*
import app.presenter.navigation.*
import app.presenter.theme.*
import com.virtual.lab.virtuallaboratory.generated.resources.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import moe.tlaster.precompose.*
import org.jetbrains.compose.resources.*
import org.koin.core.context.*
import java.awt.*
import java.awt.SystemColor.*

fun testSerialization() {
    val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
    val component = UMLClassComponent()
    val connection = UMLClassConnection(
        startRef = RefConnection.SimpleConnection(component),
        endRef = RefConnection.ReferencedConnection(component, RefType.Field(0)),
    )
    val encodedComponent = json.encodeToString(component)
    val encodedConnection = json.encodeToString(connection)

    println(encodedComponent)
    println(encodedConnection)
    println(json.decodeFromString<UMLClassComponent>(encodedComponent).toString())
    println(json.decodeFromString<UMLClassConnection>(encodedConnection).toString())
}

fun main() {
    startKoin {
        modules(
            mainKoinModule
        )
    }

//    testSerialization()

    application {
        val windowState = rememberWindowState(
            placement = WindowPlacement.Floating,
            position = WindowPosition.Aligned(Alignment.Center),
            size = DpSize(1080.dp, 720.dp)
        )

        val isSystemInDarkTheme = isSystemInDarkTheme()
        var appTheme by remember { mutableStateOf(if (isSystemInDarkTheme) Theme.DARK else Theme.LIGHT) }

        var isMaximized by remember { mutableStateOf(false) }
        var isWindowResizeable by remember { mutableStateOf(true) }
        var lastPosition by remember { mutableStateOf(windowState.position) }
        var lastSize by remember { mutableStateOf(windowState.size) }

        Window(
            state = windowState,
            icon = painterResource(Res.drawable.app_icon),
            undecorated = true,
            resizable = isWindowResizeable,
            onCloseRequest = ::exitApplication
        ) {
            window.minimumSize = Dimension(1080, 720)

            PreComposeApp {
                AppTheme(appTheme) {
                    WindowTitleBar(
                        theme = appTheme,
                        onChangeTheme = {
                            appTheme = !appTheme
                        },
                        isMaximized = isMaximized,
                        onMinimize = {
                            windowState.isMinimized = true
                        },
                        onMaximize = {
                            isMaximized = if (isMaximized) {
                                windowState.size = lastSize
                                windowState.position = lastPosition

                                isWindowResizeable = true
                                false
                            } else {
                                val insets = Toolkit.getDefaultToolkit().getScreenInsets(window.graphicsConfiguration)
                                val bounds = window.graphicsConfiguration.bounds

                                lastSize = windowState.size
                                lastPosition = windowState.position

                                windowState.size = DpSize(bounds.width.dp, (bounds.height - insets.bottom).dp)
                                windowState.position = WindowPosition(0.dp, 0.dp)

                                isWindowResizeable = false
                                true
                            }
                        },
                        onClose = {
                            exitApplication()
                        }
                    ) {
                        NavigationScreen()
                    }
                }
            }
        }
    }
}
