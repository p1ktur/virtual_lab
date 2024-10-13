package app.presenter.screens.designing.components.connectionData

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.connection.*
import app.domain.viewModels.designing.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*

@Composable
fun ClassConnectionDetailsColumn(
    modifier: Modifier = Modifier,
    index: Int,
    reference: UMLClassConnection,
    commonCounter: Int,
    onUiAction: (DesigningUiAction) -> Unit
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
                .padding(end = if (canScroll) 14.dp else 0.dp),
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
                    backgroundShown = false,
                    onClick = {
                        onUiAction(DesigningUiAction.DeleteConnection(index))
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
                    onUiAction(DesigningUiAction.UpdateConnectionData {
                        name = newValue
                    })
                },
                maxLength = 48,
                showEditIcon = false,
                textStyle = MaterialTheme.typography.bodyMedium,
                labelTextStyle = MaterialTheme.typography.bodySmall,
                textColor = Color.Black
            )
            HorizontalDivider(
                color = Color.Black,
                fillMaxWidth = 1f
            )
            ConnectionCustomizationView(
                reference = reference,
                commonCounter = commonCounter,
                onUiAction = onUiAction
            )
            if (reference.startRef.ref.fields.isNotEmpty() || reference.startRef.ref.functions.isNotEmpty()) {
                HorizontalDivider(
                    color = Color.Black,
                    fillMaxWidth = 1f
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Connect Start to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                FieldsChooseList(
                    reference = reference,
                    componentRef = reference.startRef.ref,
                    isStart = true,
                    commonCounter = commonCounter,
                    onUiAction = onUiAction
                )
                FunctionsChooseList(
                    reference = reference,
                    componentRef = reference.startRef.ref,
                    isStart = true,
                    commonCounter = commonCounter,
                    onUiAction = onUiAction
                )
            }
            if (reference.endRef.ref.fields.isNotEmpty() || reference.endRef.ref.functions.isNotEmpty()) {
                HorizontalDivider(
                    color = Color.Black,
                    fillMaxWidth = 1f
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Connect End to:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
                FieldsChooseList(
                    reference = reference,
                    componentRef = reference.endRef.ref,
                    isStart = false,
                    commonCounter = commonCounter,
                    onUiAction = onUiAction
                )
                FunctionsChooseList(
                    reference = reference,
                    componentRef = reference.endRef.ref,
                    isStart = false,
                    commonCounter = commonCounter,
                    onUiAction = onUiAction
                )
            }
        }
        if (canScroll) {
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(Color.White)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(columnScrollState),
                style = ScrollbarStyle(
                    minimalHeight = 8.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4f),
                    hoverDurationMillis = 200,
                    unhoverColor = Color.DarkGray,
                    hoverColor = Color.Gray
                )
            )
        }
    }
}