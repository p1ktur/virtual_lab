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
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.component.function.*
import app.domain.umlDiagram.classDiagram.component.function.Function
import app.domain.util.list.*
import app.domain.util.numbers.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*
import app.presenter.screens.classDiagram.components.buttons.*
import app.presenter.theme.*

@Composable
fun FunctionsList(
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
            itemsIndexed(reference.functions) { index, function ->
                FunctionView(
                    modifier = Modifier.fillMaxWidth(),
                    index = index,
                    totalFunctions = reference.functions.size,
                    function = function,
                    commonCounter = commonCounter,
                    onUpdateValue = { updater ->
                        onUiAction(ClassDiagramUiAction.UpdateComponentData {
                            functions[index].apply(updater)
                        })
                    },
                    onDelete = { id ->
                        onUiAction(ClassDiagramUiAction.DeleteFunction(id))
                    },
                    onMoveFunction = { amount ->
                        val where = (index + amount).limit(0, reference.functions.size - 1)
                        onUiAction(ClassDiagramUiAction.UpdateComponentData {
                            functions.swap(index, where)
                        })
                    }
                )
            }
            item {
                ActionButton(
                    modifier = Modifier.size(24.dp),
                    icon = Icons.Default.Add,
                    actionText = "Add Function",
                    color = LocalAppTheme.current.text,
                    onClick = {
                        onUiAction(ClassDiagramUiAction.UpdateComponentData {
                            functions += Function()
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
private fun FunctionView(
    modifier: Modifier = Modifier,
    index: Int,
    totalFunctions: Int,
    function: Function,
    commonCounter: Int,
    onUpdateValue: (Function.() -> Unit) -> Unit,
    onDelete: (Int) -> Unit,
    onMoveFunction: (Int) -> Unit
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
                        actionText = "Move Function Up",
                        color = LocalAppTheme.current.text,
                        onClick = { onMoveFunction(-1) }
                    )
                }
                if (index == totalFunctions - 1) {
                    Spacer(modifier = Modifier.size(16.dp))
                } else {
                    ActionButton(
                        modifier = Modifier.size(16.dp),
                        icon = Icons.Default.KeyboardArrowDown,
                        actionText = "Move Function Down",
                        color = LocalAppTheme.current.text,
                        onClick = { onMoveFunction(1) }
                    )
                }
            }
            ActionButton(
                modifier = Modifier.size(16.dp),
                icon = Icons.Default.Delete,
                actionText = "Delete Function",
                color = LocalAppTheme.current.text,
                onClick = { onDelete(index) }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        SingleLineTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            text = function.name,
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
            text = function.returnType,
            label = "Return Type:",
            onValueChange = { newValue ->
                onUpdateValue {
                    returnType = newValue
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
                        isHighlighted = function.visibility == visibility,
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
                isChecked = function.isStatic,
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
        key(commonCounter) {
            if (function.params.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                HorizontalDivider(
                    color = LocalAppTheme.current.text,
                    fillMaxWidth = 1f
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = "Parameters:",
                    style = MaterialTheme.typography.labelMedium,
                    color = LocalAppTheme.current.text,
                    textAlign = TextAlign.Start
                )
            }
        }
        key(function.params.size) {
            function.params.forEachIndexed { index, param ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom
                ) {
                    SingleLineTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 4.dp),
                        text = param.name,
                        label = "Name:",
                        onValueChange = { newValue ->
                            onUpdateValue {
                                function.params[index].name = newValue
                            }
                        },
                        maxLength = 48,
                        showFrame = false,
                        showEditIcon = false,
                        textStyle = MaterialTheme.typography.labelMedium,
                        labelTextStyle = MaterialTheme.typography.labelSmall,
                        textColor = LocalAppTheme.current.text
                    )
                    SingleLineTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp, end = 4.dp),
                        text = param.type,
                        label = "Type:",
                        onValueChange = { newValue ->
                            onUpdateValue {
                                function.params[index].type = newValue
                            }
                        },
                        maxLength = 48,
                        showFrame = false,
                        showEditIcon = false,
                        textStyle = MaterialTheme.typography.labelMedium,
                        labelTextStyle = MaterialTheme.typography.labelSmall,
                        textColor = LocalAppTheme.current.text
                    )
                    ActionButton(
                        modifier = Modifier
                            .padding(bottom = 4.dp, end = 4.dp)
                            .size(12.dp),
                        icon = Icons.Default.Delete,
                        actionText = "Delete Parameter",
                        color = LocalAppTheme.current.text,
                        onClick = {
                            onUpdateValue {
                                function.params.removeAt(index)
                            }
                        }
                    )
                }
                if (index != function.params.lastIndex) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.End
                    ) {
                        HorizontalDivider(
                            color = LocalAppTheme.current.text,
                            fillMaxWidth = 0.85f,
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ActionButton(
                modifier = Modifier.size(24.dp),
                icon = Icons.Default.Add,
                actionText = "Add Parameter",
                color = LocalAppTheme.current.text,
                onClick = {
                    onUpdateValue {
                        function.params += Param()
                    }
                }
            )
        }
    }
}