package app.presenter.components.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import app.presenter.components.window.*

@Composable
fun ErrorDialog(
    size: DpSize,
    title: String,
    text: String,
    visible: Boolean,
    onClose: () -> Unit
) {
    val dialogState = rememberDialogState(size = size)

    DialogWindow(
        state = dialogState,
        onCloseRequest = { onClose() },
        visible = visible,
        resizable = false,
        onKeyEvent = { event ->
            when (event.key) {
                Key.Escape -> onClose()
                else -> Unit
            }
            false
        },
        undecorated = true
    ) {
        DialogWindowTitleBar(
            title = title,
            onClose = onClose
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp),
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp, 36.dp)
                        .align(Alignment.BottomEnd),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0x3399DD),
                        contentColor = Color.White
                    ),
                    onClick = onClose
                ) {
                    Text(
                        text = "OK",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    }
}