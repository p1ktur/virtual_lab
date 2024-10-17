package app.presenter.screens.designing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.presenter.screens.designing.components.*
import app.domain.viewModels.designing.*
import app.presenter.screens.designing.components.componentData.*
import app.presenter.screens.designing.components.connectionData.*

@Composable
fun DesigningScreen(
    uiState: DesigningUiState,
    onUiAction: (DesigningUiAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when {
                uiState.focusUiState.focusedComponent != null -> uiState.focusUiState.focusedComponent.let { (ref, index) ->
                    ClassComponentDetailsColumn(
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12f))
                            .background(Color.White)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                            .padding(4.dp),
                        index = index,
                        reference = ref,
                        commonCounter = uiState.commonCounter,
                        onUiAction = onUiAction
                    )
                }
                uiState.focusUiState.focusedConnection != null -> uiState.focusUiState.focusedConnection.let { (ref, index) ->
                    ClassConnectionDetailsColumn(
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12f))
                            .background(Color.White)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                            .padding(4.dp),
                        index = index,
                        reference = ref,
                        commonCounter = uiState.commonCounter,
                        onUiAction = onUiAction
                    )
                }
                else -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12f))
                            .background(Color.White)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Select component or connection",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ToolAndActionsBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12f))
                        .background(Color.White)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                        .padding(4.dp),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
                DiagramCanvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12f))
                        .background(Color.White)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f)),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
            }
        }
    }
}