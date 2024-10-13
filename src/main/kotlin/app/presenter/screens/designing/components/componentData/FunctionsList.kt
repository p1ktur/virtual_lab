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
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.component.function.*
import app.domain.umlDiagram.model.component.function.Function
import app.domain.util.list.*
import app.domain.util.numbers.*
import app.domain.viewModels.designing.*
import app.presenter.components.buttons.*
import app.presenter.components.common.*

@Composable
fun FunctionsList(
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
            itemsIndexed(reference.functions) { index, function ->
                FunctionView(
                    modifier = Modifier.fillMaxWidth(),
                    index = index,
                    totalFunctions = reference.functions.size,
                    function = function,
                    commonCounter = commonCounter,
                    onUpdateValue = { updater ->
                        onUiAction(DesigningUiAction.UpdateComponentData {
                            functions[index].apply(updater)
                        })
                    },
                    onDelete = { id ->
                        onUiAction(DesigningUiAction.DeleteFunction(id))
                    },
                    onMoveFunction = { amount ->
                        val where = (index + amount).limit(0, reference.functions.size - 1)
                        onUiAction(DesigningUiAction.UpdateComponentData {
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
                    backgroundShown = false,
                    onClick = {
                        onUiAction(DesigningUiAction.UpdateComponentData {
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
            .clip(RoundedCornerShape(8f))
            .background(Color.White)
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
                color = Color.Black
            )
            Row {
                if (index == 0) {
                    Spacer(modifier = Modifier.size(16.dp))
                } else {
                    ActionButton(
                        modifier = Modifier.size(16.dp),
                        icon = Icons.Default.KeyboardArrowUp,
                        actionText = "Move Function Up",
                        backgroundShown = false,
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
                        backgroundShown = false,
                        onClick = { onMoveFunction(1) }
                    )
                }
            }
            ActionButton(
                modifier = Modifier.size(16.dp),
                icon = Icons.Default.Delete,
                actionText = "Delete Function",
                backgroundShown = false,
                onClick = { onDelete(index) }
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        DefaultTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            startValue = function.name,
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
            startValue = function.returnType,
            label = "Return Type:",
            onValueChange = { newValue ->
                onUpdateValue {
                    returnType = newValue
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
                    color = Color.Black,
                    fillMaxWidth = 1f
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    text = "Parameters:",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Black,
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
                    DefaultTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp, end = 4.dp),
                        startValue = param.name,
                        label = "Name:",
                        onValueChange = { newValue ->
                            onUpdateValue {
                                function.params[index].name = newValue
                            }
                        },
                        maxLength = 48,
                        showEditIcon = false,
                        textStyle = MaterialTheme.typography.labelMedium,
                        labelTextStyle = MaterialTheme.typography.labelSmall,
                        textColor = Color.Black
                    )
                    DefaultTextField(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp, end = 4.dp),
                        startValue = param.type,
                        label = "Type:",
                        onValueChange = { newValue ->
                            onUpdateValue {
                                function.params[index].type = newValue
                            }
                        },
                        maxLength = 48,
                        showEditIcon = false,
                        textStyle = MaterialTheme.typography.labelMedium,
                        labelTextStyle = MaterialTheme.typography.labelSmall,
                        textColor = Color.Black
                    )
                    ActionButton(
                        modifier = Modifier
                            .padding(bottom = 4.dp, end = 4.dp)
                            .size(16.dp),
                        icon = Icons.Default.Delete,
                        actionText = "Delete Parameter",
                        backgroundShown = false,
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HorizontalDivider(
                            color = Color.Black,
                            fillMaxWidth = 0.85f
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
                backgroundShown = false,
                onClick = {
                    onUpdateValue {
                        function.params += Param()
                    }
                }
            )
        }
    }
}