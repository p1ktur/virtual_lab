package app.presenter.components.common

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import app.presenter.theme.*

@Composable
fun LoadingText() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppTheme.current.screenTwo),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(end = 16.dp),
            text = "Loading...",
            style = MaterialTheme.typography.titleMedium,
            color = LocalAppTheme.current.text
        )
        CircularProgressIndicator(
            modifier = Modifier.size(32.dp),
            color = LocalAppTheme.current.text,
            strokeWidth = 4.dp
        )
    }
}