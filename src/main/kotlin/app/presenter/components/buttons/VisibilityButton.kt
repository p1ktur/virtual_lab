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
import app.domain.umlDiagram.model.component.*
import app.presenter.components.tooltip.*

@Composable
fun VisibilityButton(
    visibility: Visibility,
    isHighlighted: Boolean,
    onClick: () -> Unit,
) {
    TooltipOn(
        text = when (visibility) {
            Visibility.PUBLIC -> "Public"
            Visibility.PRIVATE -> "Private"
            Visibility.PROTECTED -> "Protected"
            Visibility.PACKAGE -> "Package"
        }
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .run {
                    if (isHighlighted) {
                        border(0.5.dp, Color.Black, CircleShape)
                    } else this
                }
                .padding(2.dp)
                .clip(CircleShape)
                .background(
                    when (visibility) {
                        Visibility.PUBLIC -> Color(0xFF2E6DD1)
                        Visibility.PRIVATE -> Color(0xFFDB3941)
                        Visibility.PROTECTED -> Color(0xFFDB7A39)
                        Visibility.PACKAGE -> Color(0xFF888995)
                    }
                )
                .border(0.5.dp, Color.Black, CircleShape)
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
                color = Color.White
            )
        }
    }
}