package app.presenter.components.buttons

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

@Composable
fun Checker(
    modifier: Modifier = Modifier,
    label: String,
    isChecked: Boolean,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    activeColor: Color = MaterialTheme.colorScheme.onBackground,
    inactiveColor: Color = MaterialTheme.colorScheme.primary,
    onChecked: (Boolean) -> Unit
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(
                    color = if (isChecked) activeColor else inactiveColor,
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isChecked) activeColor else inactiveColor,
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable { onChecked(!isChecked) }
        ) {
            if (isChecked) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.Done,
                    contentDescription = "Checker state",
                    tint = inactiveColor
                )
            }
        }
    }
}