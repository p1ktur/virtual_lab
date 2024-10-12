package app.presenter.screens.designing.components.componentData

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.component.*
import app.domain.viewModels.designing.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*

@Composable
fun FieldsList(
    modifier: Modifier,
    reference: UMLClassComponent,
    commonCounter: Int,
    onUiAction: (DesigningUiAction) -> Unit
) {
    val maxListHeight = 256.dp

    val lazyColumnState = rememberLazyListState()
    val canScroll by remember {
        derivedStateOf {
            lazyColumnState.canScrollForward || lazyColumnState.canScrollBackward
        }
    }

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8f))
                .background(Color.LightGray)
                .padding(6.dp)
                .padding(end = if (canScroll) 12.dp else 0.dp)
                .heightIn(0.dp, maxListHeight),
            state = lazyColumnState,
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            itemsIndexed(reference.fields) { index, field ->
                FieldView(
                    modifier = Modifier.fillMaxWidth(),
                    index = index,
                    field = field,
                    commonCounter = commonCounter,
                    onUpdateValue = { updater ->
                        onUiAction(DesigningUiAction.UpdateComponentData {
                            fields[index].apply(updater)
                        })
                    }
                )
            }
            item {
                ActionButton(
                    modifier = Modifier.size(24.dp),
                    icon = Icons.Default.Add,
                    actionText = "Add Field",
                    onClick = {
                        onUiAction(DesigningUiAction.UpdateComponentData {
                            fields += Field()
                        })
                    }
                )
            }
        }
        if (canScroll) {
            VerticalScrollbar(
                modifier = Modifier
                    .height(maxListHeight)
                    .width(12.dp)
                    .background(Color.LightGray)
                    .padding(all = 4.dp)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(lazyColumnState),
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

@Composable
private fun FieldView(
    modifier: Modifier = Modifier,
    index: Int,
    field: Field,
    commonCounter: Int,
    onUpdateValue: (Field.() -> Unit) -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(8f))
                .background(Color.White)
                .padding(vertical = 4.dp)
        ) {
            DefaultTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                startValue = field.name,
                label = "Name:",
                onValueChange = { newValue ->
                    onUpdateValue {
                        name = newValue
                    }
                },
                maxLength = 48,
                showEditIcon = false,
                textStyle = MaterialTheme.typography.bodySmall,
                labelTextStyle = MaterialTheme.typography.labelSmall,
                textColor = Color.Black
            )
            DefaultTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                startValue = field.type,
                label = "Type:",
                onValueChange = { newValue ->
                    onUpdateValue {
                        type = newValue
                    }
                },
                maxLength = 48,
                showEditIcon = false,
                textStyle = MaterialTheme.typography.bodySmall,
                labelTextStyle = MaterialTheme.typography.labelSmall,
                textColor = Color.Black
            )
            key(commonCounter) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp, horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Visibility:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                    Visibility.entries.forEach { visibility ->
                        VisibilityButton(
                            visibility = visibility,
                            isHighlighted = field.visibility == visibility,
                            onClick = {
                                onUpdateValue {
                                    this.visibility = visibility
                                }
                            }
                        )
                    }
                }
                Checker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    label = "Static:",
                    isChecked = field.isStatic,
                    onChecked = { newValue ->
                        onUpdateValue {
                            isStatic = newValue
                        }
                    }
                )
            }
        }
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 1.dp, end = 4.dp),
            text = "(${index + 1})",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black
        )
    }
}