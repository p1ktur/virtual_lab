package app.presenter.screens.classDiagram.components.componentData

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*

@Composable
fun ClassComponentDetailsColumn(
    modifier: Modifier = Modifier,
    index: Int,
    reference: UMLClassComponent,
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
                    actionText = "Delete Component",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = {
                        onUiAction(ClassDiagramUiAction.DeleteComponent(index))
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
                    onUiAction(ClassDiagramUiAction.UpdateComponentData {
                        name = newValue
                    })
                },
                maxLength = 48,
                showEditIcon = false,
                textStyle = MaterialTheme.typography.bodyMedium,
                labelTextStyle = MaterialTheme.typography.labelLarge,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fillMaxWidth = 1f
            )
            key(commonCounter) {
                Checker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    label = "Is Interface:",
                    isChecked = reference.isInterface,
                    textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    otherContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    iconColor = MaterialTheme.colorScheme.primaryContainer,
                    onChecked = { newValue ->
                        onUiAction(ClassDiagramUiAction.UpdateComponentData {
                            isInterface = newValue
                        })
                    }
                )
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fillMaxWidth = 1f
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = "Fields",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Start
            )
            FieldsList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                reference = reference,
                commonCounter = commonCounter,
                onUiAction = onUiAction
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fillMaxWidth = 1f
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = "Functions",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Start
            )
            FunctionsList(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                reference = reference,
                commonCounter = commonCounter,
                onUiAction = onUiAction
            )
            Spacer(modifier = Modifier.height(48.dp))
        }
        if (canScroll) {
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(end = 1.dp)
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