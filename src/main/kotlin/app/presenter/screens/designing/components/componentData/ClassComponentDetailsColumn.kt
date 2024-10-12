package app.presenter.screens.designing.components.componentData

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.component.*
import app.domain.viewModels.designing.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*

@Composable
fun ClassComponentDetailsColumn(
    modifier: Modifier = Modifier,
    reference: UMLClassComponent,
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
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(columnScrollState)
                .padding(end = if (canScroll) 14.dp else 0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                startValue = reference.name,
                label = "Name:",
                onValueChange = { newValue ->
                    onUiAction(DesigningUiAction.UpdateComponentData {
                        name = newValue
                    })
                },
                maxLength = 48,
                showEditIcon = false,
                textStyle = MaterialTheme.typography.bodyMedium,
                labelTextStyle = MaterialTheme.typography.labelLarge,
                textColor = Color.Black
            )
            HorizontalDivider(
                color = Color.Black,
                fillMaxWidth = 1f
            )
            key(commonCounter) {
                Checker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 4.dp),
                    label = "Is Interface:",
                    isChecked = reference.isInterface,
                    onChecked = { newValue ->
                        onUiAction(DesigningUiAction.UpdateComponentData {
                            isInterface = newValue
                        })
                    }
                )
            }
            HorizontalDivider(
                color = Color.Black,
                fillMaxWidth = 1f
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = "Fields",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Black,
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
                color = Color.Black,
                fillMaxWidth = 1f
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                text = "Functions",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Black,
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