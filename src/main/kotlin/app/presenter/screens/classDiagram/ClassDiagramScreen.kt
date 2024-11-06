package app.presenter.screens.classDiagram

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.style.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.presenter.components.common.*
import app.presenter.screens.classDiagram.components.*
import app.presenter.screens.classDiagram.components.componentData.*
import app.presenter.screens.classDiagram.components.connectionData.*
import app.presenter.theme.*
import app.test.*

@Composable
fun ClassDiagramScreen(
    uiState: ClassDiagramUiState,
    onUiAction: (ClassDiagramUiAction) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            when {
                uiState.focusUiState.focusedComponent != null -> uiState.focusUiState.focusedComponent.let { (ref, index) ->
                    ClassComponentDetailsColumn(
                        modifier = Modifier
                            .fillMaxWidth(0.25f)
                            .fillMaxHeight()
                            .background(LocalAppTheme.current.primaryScreenTwo),
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
                            .background(LocalAppTheme.current.primaryScreenTwo),
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
                            .background(LocalAppTheme.current.primaryScreenTwo),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Select component or connection",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalAppTheme.current.primaryScreenText,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            VerticalDivider(
                fillMaxHeight = 1f,
                color = LocalAppTheme.current.primaryScreenDivider,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ToolAndActionsBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LocalAppTheme.current.primaryScreenThree)
                        .addTestTag("Tools and Actions Bar"),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
                HorizontalDivider(
                    fillMaxWidth = 1f,
                    color = LocalAppTheme.current.primaryScreenDivider,
                )
                ClassDiagramCanvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                        .background(LocalAppTheme.current.primaryScreenThree),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
            }
        }
    }
}