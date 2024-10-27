package app.presenter.screens.classDiagram

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
import app.domain.viewModels.diagrams.classDiagram.*
import app.presenter.screens.classDiagram.components.*
import app.presenter.screens.classDiagram.components.componentData.*
import app.presenter.screens.classDiagram.components.connectionData.*
import app.test.*

@Composable
fun ClassDiagramScreen(
    uiState: ClassDiagramUiState,
    onUiAction: (ClassDiagramUiAction) -> Unit
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
                            .shadow(8.dp, RoundedCornerShape(12f))
                            .clip(RoundedCornerShape(12f))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f)),
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
                            .shadow(8.dp, RoundedCornerShape(12f))
                            .clip(RoundedCornerShape(12f))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f)),
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
                            .shadow(8.dp, RoundedCornerShape(12f))
                            .clip(RoundedCornerShape(12f))
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Select component or connection",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
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
                        .shadow(8.dp, RoundedCornerShape(12f))
                        .clip(RoundedCornerShape(12f))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                        .padding(4.dp)
                        .addTestTag("Tools and Actions Bar"),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
                ClassDiagramCanvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(8.dp, RoundedCornerShape(12f))
                        .clip(RoundedCornerShape(12f))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f)),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
            }
        }
    }
}