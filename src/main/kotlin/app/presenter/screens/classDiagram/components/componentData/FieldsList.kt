package app.presenter.screens.classDiagram.components.componentData

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
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.util.list.*
import app.domain.util.numbers.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*
import app.presenter.screens.classDiagram.components.buttons.*
import app.presenter.theme.*

@Composable
fun FieldsList(
    modifier: Modifier,
    reference: UMLClassComponent,
    commonCounter: Int,
    onUiAction: (ClassDiagramUiAction) -> Unit
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
                .shadow(4.dp, RoundedCornerShape(8f))
                .clip(RoundedCornerShape(8f))
                .background(LocalAppTheme.current.container)
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
                    totalFields = reference.fields.size,
                    field = field,
                    commonCounter = commonCounter,
                    onUpdateValue = { updater ->
                        onUiAction(ClassDiagramUiAction.UpdateComponentData {
                            fields[index].apply(updater)
                        })
                    },
                    onDelete = { id ->
                        onUiAction(ClassDiagramUiAction.DeleteField(id))
                    },
                    onMoveField = { amount ->
                        val where = (index + amount).limit(0, reference.fields.size - 1)
                        onUiAction(ClassDiagramUiAction.UpdateComponentData {
                            fields.swap(index, where)
                        })
                    }
                )
            }
            item {
                ActionButton(
                    modifier = Modifier.size(24.dp),
                    icon = Icons.Default.Add,
                    actionText = "Add Field",
                    color = LocalAppTheme.current.text,
                    onClick = {
                        onUiAction(ClassDiagramUiAction.UpdateComponentData {
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
                    .background(LocalAppTheme.current.container)
                    .padding(all = 4.dp)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(lazyColumnState),
                style = ScrollbarStyle(
                    minimalHeight = 8.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4f),
                    hoverDurationMillis = 200,
                    unhoverColor = LocalAppTheme.current.textDimmed,
                    hoverColor = LocalAppTheme.current.textDimmedInverse
                )
            )
        }
    }
}

@Composable
private fun FieldView(
    modifier: Modifier = Modifier,
    index: Int,
    totalFields: Int,
    field: Field,
    commonCounter: Int,
    onUpdateValue: (Field.() -> Unit) -> Unit,
    onDelete: (Int) -> Unit,
    onMoveField: (Int) -> Unit
) {
    Column(
        modifier = modifier
            .shadow(2.dp, RoundedCornerShape(8f))
            .clip(RoundedCornerShape(8f))
            .background(LocalAppTheme.current.container)
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "(${index + 1})",
                style = MaterialTheme.typography.labelSmall,
                color = LocalAppTheme.current.text
            )
            Row {
                if (index == 0) {
                    Spacer(modifier = Modifier.size(16.dp))
                } else {
                    ActionButton(
                        modifier = Modifier.size(16.dp),
                        icon = Icons.Default.KeyboardArrowUp,
                        actionText = "Move Field Up",
                        color = LocalAppTheme.current.text,
                        onClick = { onMoveField(-1) }
                    )
                }
                if (index == totalFields - 1) {
                    Spacer(modifier = Modifier.size(16.dp))
                } else {
                    ActionButton(
                        modifier = Modifier.size(16.dp),
                        icon = Icons.Default.KeyboardArrowDown,
                        actionText = "Move Field Down",
                        color = LocalAppTheme.current.text,
                        onClick = { onMoveField(1) }
                    )
                }
            }
            ActionButton(
                modifier = Modifier.size(16.dp),
                icon = Icons.Default.Delete,
                actionText = "Delete Field",
                color = LocalAppTheme.current.text,
                onClick = { onDelete(index) }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        SingleLineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = field.name,
            label = "Name:",
            onValueChange = { newValue ->
                onUpdateValue {
                    name = newValue
                }
            },
            maxLength = 48,
            showFrame = false,
            showEditIcon = false,
            textStyle = MaterialTheme.typography.bodySmall,
            labelTextStyle = MaterialTheme.typography.bodySmall,
            textColor = LocalAppTheme.current.text
        )
        SingleLineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = field.type,
            label = "Type:",
            onValueChange = { newValue ->
                onUpdateValue {
                    type = newValue
                }
            },
            maxLength = 48,
            showFrame = false,
            showEditIcon = false,
            textStyle = MaterialTheme.typography.bodySmall,
            labelTextStyle = MaterialTheme.typography.bodySmall,
            textColor = LocalAppTheme.current.text
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
                    color = LocalAppTheme.current.text
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
                textColor = LocalAppTheme.current.text,
                otherContentColor = LocalAppTheme.current.text,
                iconColor = LocalAppTheme.current.container,
                onChecked = { newValue ->
                    onUpdateValue {
                        isStatic = newValue
                    }
                }
            )
        }
    }
}