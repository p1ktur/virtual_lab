package app.presenter.screens.classDiagram

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.auth.*
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
                            .background(LocalAppTheme.current.screenTwo),
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
                            .background(LocalAppTheme.current.screenTwo),
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
                            .background(LocalAppTheme.current.screenTwo),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Select component or connection",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalAppTheme.current.text,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            VerticalDivider(
                fillMaxHeight = 1f,
                color = LocalAppTheme.current.divider,
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
                        .background(LocalAppTheme.current.screenThree)
                        .addTestTag("Tools and Actions Bar"),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
                HorizontalDivider(
                    fillMaxWidth = 1f,
                    color = LocalAppTheme.current.divider,
                )
                ClassDiagramCanvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipToBounds()
                        .background(LocalAppTheme.current.screenThree),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(0.25f)
                .align(Alignment.BottomStart),
            horizontalArrangement = Arrangement.Center
        ) {
            app.presenter.components.buttons.TextButton(
                modifier = Modifier.padding(bottom = 24.dp),
                text = if (uiState.authType is AuthType.Student) "Submit" else "Save",
                color = LocalAppTheme.current.text,
                backgroundColor = LocalAppTheme.current.container,
                onClick = {
                    onUiAction(ClassDiagramUiAction.SaveChanges)
                }
            )
        }
    }
}