package app.presenter.components.buttons

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.WindowPosition.PlatformDefault.x
import androidx.compose.ui.window.WindowPosition.PlatformDefault.y
import app.domain.umlDiagram.model.component.*

@Composable
fun VisibilityButton(
    visibility: Visibility,
    isHighlighted: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(
                when (visibility) {
                    Visibility.PUBLIC -> Color(0xFF2E6DD1)
                    Visibility.PRIVATE -> Color(0xFFDB3941)
                    Visibility.PROTECTED -> Color(0xFFDB7A39)
                    Visibility.PACKAGE -> Color(0xFF888995)
                }
            )
            .border(0.5.dp, if (isHighlighted) Color.Green else Color.Black, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (visibility) {
                Visibility.PUBLIC -> "+"
                Visibility.PRIVATE -> "-"
                Visibility.PROTECTED -> "#"
                Visibility.PACKAGE -> "~"
            },
            style = MaterialTheme.typography.bodySmall,
            color = if (isHighlighted) Color.Green else Color.White
        )
    }
}