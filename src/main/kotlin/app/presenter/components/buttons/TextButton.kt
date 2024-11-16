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

@Composable
fun TextButton(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    backgroundColor: Color = Color.Transparent,
    shape: Shape = RoundedCornerShape(50),
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .defaultMinSize(144.dp, 36.dp)
            .clip(shape)
            .run {
                if (backgroundColor != Color.Transparent) {
                    this.background(backgroundColor)
                        .border(0.5.dp, color, shape)
                } else {
                    this
                }
            }
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}