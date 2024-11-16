package app.presenter.components.tooltip

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipOn(
    text: String,
    content: @Composable () -> Unit
) {
    TooltipArea(
        tooltip = {
            Text(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(4f))
                    .border(0.5.dp, Color.Black, RoundedCornerShape(4f))
                    .padding(vertical = 2.dp, horizontal = 4.dp),
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black
            )
        },
        tooltipPlacement = TooltipPlacement.CursorPoint(
            offset = DpOffset(8.dp, 12.dp)
        ),
        content = content
    )
}