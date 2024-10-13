package app.presenter.components.buttons

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.editing.*
import app.presenter.components.tooltip.*

@Composable
fun EditModeButton(
    modifier: Modifier,
    icon: ImageVector,
    editMode: EditMode,
    connectedEditMode: EditMode,
    onClick: () -> Unit
) {
    TooltipOn(
        text = when (connectedEditMode) {
            EditMode.SELECTOR -> "Selector Tool"
            EditMode.CONNECTOR -> "Connector Tool"
        }
    ) {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .background(Color.White)
                .clickable(onClick = onClick)
                .run {
                    if (editMode == connectedEditMode) {
                        border(0.5.dp, Color.Black, CircleShape)
                    } else this
                }
                .padding(3.dp)
                .border(0.5.dp, Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(12.dp),
                imageVector = icon,
                contentDescription = "Edit mode button",
                tint = Color.Black
            )
        }
    }
}