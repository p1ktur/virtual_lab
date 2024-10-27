package app.presenter.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.unit.*
import app.domain.viewModels.task.*

@Composable
fun TaskScreen(
    uiState: TaskUiState,
    onUiAction: (TaskUiAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .shadow(8.dp, RoundedCornerShape(12f))
            .clip(RoundedCornerShape(12f))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f)),
        contentAlignment = Alignment.Center
    ) {

    }
}