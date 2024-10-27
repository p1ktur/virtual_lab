package app.presenter.screens.classDiagram.components.connectionData

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*

@Composable
fun ClassConnectionDetailsColumn(
    modifier: Modifier = Modifier,
    index: Int,
    reference: UMLClassConnection,
    commonCounter: Int,
    onUiAction: (ClassDiagramUiAction) -> Unit
) {
    val columnScrollState = rememberScrollState()
    val canScroll by remember {
        derivedStateOf {
            columnScrollState.canScrollForward || columnScrollState.canScrollBackward
        }
    }

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(columnScrollState)
                .padding(4.dp)
                .padding(end = if (canScroll) 8.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(
                    modifier = Modifier.size(24.dp),
                    icon = Icons.Default.Delete,
                    actionText = "Delete Connection",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = {
                        onUiAction(ClassDiagramUiAction.DeleteConnection(index))
                    }
                )
            }
            DefaultTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                startValue = reference.name,
                label = "Name:",
                onValueChange = { newValue ->
                    onUiAction(ClassDiagramUiAction.UpdateConnectionData {
                        name = newValue
                    })
                },
                maxLength = 48,
                showEditIcon = false,
                textStyle = MaterialTheme.typography.bodyMedium,
                labelTextStyle = MaterialTheme.typography.bodySmall,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fillMaxWidth = 1f
            )
            ConnectionCustomizationView(
                reference = reference,
                commonCounter = commonCounter,
                onUiAction = onUiAction
            )
            if (reference.startRef.getRefClass().fields.isNotEmpty() || reference.startRef.getRefClass().functions.isNotEmpty()) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fillMaxWidth = 1f
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Connect Start to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                FieldsChooseList(
                    reference = reference,
                    componentRef = reference.startRef.getRefClass(),
                    isStart = true,
                    commonCounter = commonCounter,
                    onUiAction = onUiAction
                )
                FunctionsChooseList(
                    reference = reference,
                    componentRef = reference.startRef.getRefClass(),
                    isStart = true,
                    commonCounter = commonCounter,
                    onUiAction = onUiAction
                )
            }
            if (reference.endRef.getRefClass().fields.isNotEmpty() || reference.endRef.getRefClass().functions.isNotEmpty()) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fillMaxWidth = 1f
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Connect End to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                FieldsChooseList(
                    reference = reference,
                    componentRef = reference.endRef.getRefClass(),
                    isStart = false,
                    commonCounter = commonCounter,
                    onUiAction = onUiAction
                )
                FunctionsChooseList(
                    reference = reference,
                    componentRef = reference.endRef.getRefClass(),
                    isStart = false,
                    commonCounter = commonCounter,
                    onUiAction = onUiAction
                )
            }
            Spacer(modifier = Modifier.height(48.dp))
        }
        if (canScroll) {
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(columnScrollState),
                style = ScrollbarStyle(
                    minimalHeight = 8.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4f),
                    hoverDurationMillis = 200,
                    unhoverColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    hoverColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}