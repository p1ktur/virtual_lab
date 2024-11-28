package app.presenter.components.window

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.window.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import app.presenter.components.common.*
import app.presenter.theme.*
import com.virtual.lab.virtuallaboratory.generated.resources.*
import com.virtual.lab.virtuallaboratory.generated.resources.Res
import org.jetbrains.compose.resources.*

@Composable
fun WindowScope.WindowTitleBar(
    theme: Theme,
    onChangeTheme: () -> Unit,
    isMaximized: Boolean,
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        if (isMaximized) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(LocalAppTheme.current.screenZero),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TitleBarContent(
                    theme = theme,
                    onChangeTheme = onChangeTheme,
                    onMinimize = onMinimize,
                    onMaximize = onMaximize,
                    onClose = onClose
                )
            }
        } else {
            WindowDraggableArea(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(LocalAppTheme.current.screenZero)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TitleBarContent(
                        theme = theme,
                        onChangeTheme = onChangeTheme,
                        onMinimize = onMinimize,
                        onMaximize = onMaximize,
                        onClose = onClose
                    )
                }
            }
        }
        HorizontalDivider(
            fillMaxWidth = 1f,
            color = LocalAppTheme.current.divider
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(LocalAppTheme.current.background),
            content = content
        )
    }
}

@Composable
private fun TitleBarContent(
    theme: Theme,
    onChangeTheme: () -> Unit,
    onMinimize: () -> Unit,
    onMaximize: () -> Unit,
    onClose: () -> Unit
) {
    Row {
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 4.dp, horizontal = 8.dp),
            painter = painterResource(Res.drawable.app_icon),
            contentDescription = "App icon",
            colorFilter = ColorFilter.tint(LocalAppTheme.current.text, BlendMode.SrcAtop)
        )
        Text(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            text = "Virtual Laboratory",
            style = MaterialTheme.typography.bodySmall,
            color = LocalAppTheme.current.text
        )
    }
    Row {
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable(onClick = onChangeTheme)
                .padding(4.dp),
            imageVector = when (theme) {
                Theme.LIGHT -> Icons.Default.LightMode
                Theme.DARK -> Icons.Default.DarkMode
            },
            contentDescription = "Change theme",
            tint = LocalAppTheme.current.text
        )
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable(onClick = onMinimize)
                .padding(4.dp),
            imageVector = Icons.Default.Minimize,
            contentDescription = "Minimize app",
            tint = LocalAppTheme.current.text
        )
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable(onClick = onMaximize)
                .padding(4.dp),
            imageVector = Icons.Default.Fullscreen,
            contentDescription = "Maximize app",
            tint = LocalAppTheme.current.text
        )
        Icon(
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clickable(onClick = onClose)
                .padding(4.dp),
            imageVector = Icons.Default.Close,
            contentDescription = "Close app",
            tint = LocalAppTheme.current.text
        )
    }
}