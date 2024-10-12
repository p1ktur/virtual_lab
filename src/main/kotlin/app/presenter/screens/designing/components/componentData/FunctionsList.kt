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
import app.domain.umlDiagram.model.component.Function
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
                    function = function,
                    commonCounter = commonCounter,
                    onUpdateValue = { updater ->
                        onUiAction(DesigningUiAction.UpdateComponentData {
                            functions[index].apply(updater)
                        })
                    }
                )
            }
            item {
                ActionButton(
                    modifier = Modifier.size(24.dp),
                    icon = Icons.Default.Add,
                    actionText = "Add Function",
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
    function: Function,
    commonCounter: Int,
    onUpdateValue: (Function.() -> Unit) -> Unit,
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DefaultTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp),
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
                                .padding(horizontal = 8.dp),
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                ActionButton(
                    modifier = Modifier.size(24.dp),
                    icon = Icons.Default.Add,
                    actionText = "Add Parameter",
                    backgroundShown = false,
                    onClick = {
                        onUpdateValue {
                            function.params += Function.Param()
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.padding(4.dp))
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