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
import app.presenter.theme.*

@Composable
fun TaskScreen(
    uiState: TaskUiState,
    onUiAction: (TaskUiAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppTheme.current.primaryScreenTwo),
        contentAlignment = Alignment.Center
    ) {

    }
}