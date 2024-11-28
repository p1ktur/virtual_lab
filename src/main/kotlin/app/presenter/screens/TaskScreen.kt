package app.presenter.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.auth.*
import app.domain.viewModels.task.*
import app.presenter.components.common.*
import app.presenter.theme.*
import java.text.*

@Composable
fun TaskScreen(
    uiState: TaskUiState,
    onUiAction: (TaskUiAction) -> Unit
) {
    val canEdit = remember(uiState.authType) {
        uiState.authType is AuthType.Teacher || uiState.authType is AuthType.Administrator
    }
    val taskIsCreated = remember(uiState.task) { uiState.task.id > 0 }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppTheme.current.screenTwo),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (canEdit && taskIsCreated) {
                    app.presenter.components.buttons.IconButton(
                        modifier = Modifier.size(28.dp),
                        icon = Icons.Filled.Delete,
                        actionText = "Delete Task",
                        color = LocalAppTheme.current.text,
                        backgroundColor = Color.Transparent,
                        shape = RectangleShape,
                        onClick = {
                            onUiAction(TaskUiAction.DeleteTask)
                        }
                    )
                }
            }
            if (canEdit) {
                SingleLineTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = uiState.task.name,
                    label = "Name:",
                    onValueChange = { newValue ->
                        onUiAction(TaskUiAction.UpdateName(newValue))
                    },
                    maxLength = 48,
                    showEditIcon = false,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    labelTextStyle = MaterialTheme.typography.labelLarge,
                    textColor = LocalAppTheme.current.text
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Name: ${uiState.task.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.text,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (canEdit) {
                MultiLineTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = uiState.task.description,
                    label = "Description:",
                    placeholder = "Type description:",
                    onValueChange = { newValue ->
                        onUiAction(TaskUiAction.UpdateDescription(newValue))
                    },
                    maxLength = 1920,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    labelTextStyle = MaterialTheme.typography.labelLarge,
                    placeholderTextStyle = MaterialTheme.typography.labelLarge,
                    textColor = LocalAppTheme.current.text
                )
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Description: ${uiState.task.description}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.text
                )
            }
            if (canEdit) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 6.dp, end = 12.dp),
                        text = "Max Attempts:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalAppTheme.current.text
                    )
                    app.presenter.components.buttons.IconButton(
                        modifier = Modifier.size(28.dp),
                        icon = Icons.Filled.Remove,
                        actionText = "Sub Max Attempts",
                        color = LocalAppTheme.current.text,
                        backgroundColor = Color.Transparent,
                        shape = RectangleShape,
                        onClick = {
                            onUiAction(TaskUiAction.SubMaxAttempts)
                        }
                    )
                    SingleLineTextField(
                        modifier = Modifier
                            .width(72.dp)
                            .padding(horizontal = 4.dp),
                        text = uiState.task.maxAttempts.toString(),
                        label = "",
                        filter = TextFieldFilter.OnlyNumbers,
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { maxAttempts ->
                                onUiAction(TaskUiAction.UpdateMaxAttempts(maxAttempts))
                            }
                        },
                        maxLength = 4,
                        showEditIcon = false,
                        showFrame = false,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        labelTextStyle = MaterialTheme.typography.labelLarge,
                        textColor = LocalAppTheme.current.text
                    )
                    app.presenter.components.buttons.IconButton(
                        modifier = Modifier.size(28.dp),
                        icon = Icons.Filled.Add,
                        actionText = "Add Max Attempts",
                        color = LocalAppTheme.current.text,
                        backgroundColor = Color.Transparent,
                        shape = RectangleShape,
                        onClick = {
                            onUiAction(TaskUiAction.AddMaxAttempts)
                        }
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    text = "Max Attempts: ${uiState.task.maxAttempts}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.text
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (canEdit) {
                Row(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SingleLineTextField(
                        modifier = Modifier
                            .width(240.dp)
                            .padding(horizontal = 4.dp),
                        text = uiState.task.minMark.toString(),
                        label = "Pass Mark:",
                        filter = TextFieldFilter.OnlyNumbers,
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { passMark ->
                                onUiAction(TaskUiAction.UpdatePassMark(passMark))
                            }
                        },
                        maxLength = 4,
                        showEditIcon = false,
                        showFrame = false,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        labelTextStyle = MaterialTheme.typography.bodyLarge,
                        textColor = LocalAppTheme.current.text
                    )
                    SingleLineTextField(
                        modifier = Modifier
                            .width(240.dp)
                            .padding(horizontal = 4.dp),
                        text = uiState.task.maxMark.toString(),
                        label = "Max Mark:",
                        filter = TextFieldFilter.OnlyNumbers,
                        onValueChange = { newValue ->
                            newValue.toIntOrNull()?.let { maxMark ->
                                onUiAction(TaskUiAction.UpdateMaxMark(maxMark))
                            }
                        },
                        maxLength = 4,
                        showEditIcon = false,
                        showFrame = false,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        labelTextStyle = MaterialTheme.typography.bodyLarge,
                        textColor = LocalAppTheme.current.text
                    )
                }

            } else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "Pass Mark: ${uiState.task.minMark}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalAppTheme.current.text
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "Max Mark: ${uiState.task.maxMark}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalAppTheme.current.text
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(
                color = LocalAppTheme.current.text,
                fillMaxWidth = 1f
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = if (uiState.authType is AuthType.Student) "Your Attempts" else "Student Attempts",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.text
                )
                if (canEdit) {
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "Attempts left: ${uiState.task.maxAttempts - uiState.studentTaskAttempts.size}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalAppTheme.current.text
                    )
                }
            }
            if (uiState.studentTaskAttempts.isEmpty()) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 4.dp, vertical = 6.dp),
                    text = "No Attempts Yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalAppTheme.current.text
                )
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        text = "№",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text,
                        textAlign = TextAlign.Center
                    )
                    VerticalDivider(
                        color = LocalAppTheme.current.divider,
                        fillMaxHeight = 0.85f
                    )
                    Text(
                        modifier = Modifier
                            .weight(4f)
                            .padding(horizontal = 2.dp),
                        text = "Attempt date",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text,
                        textAlign = TextAlign.Center
                    )
                    VerticalDivider(
                        color = LocalAppTheme.current.divider,
                        fillMaxHeight = 0.85f
                    )
                    Text(
                        modifier = Modifier
                            .weight(3f)
                            .padding(horizontal = 2.dp),
                        text = "Is Success",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text,
                        textAlign = TextAlign.Center
                    )
                    VerticalDivider(
                        color = LocalAppTheme.current.divider,
                        fillMaxHeight = 0.85f
                    )
                    Text(
                        modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = 2.dp),
                        text = "Mark",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text,
                        textAlign = TextAlign.Center
                    )
                    VerticalDivider(
                        color = LocalAppTheme.current.divider,
                        fillMaxHeight = 0.85f
                    )
                    Text(
                        modifier = Modifier
                            .weight(4f)
                            .padding(horizontal = 2.dp),
                        text = "Name",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text,
                        textAlign = TextAlign.Center
                    )
                }
                HorizontalDivider(
                    color = LocalAppTheme.current.divider,
                    fillMaxWidth = 1f
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    contentPadding = PaddingValues(vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    itemsIndexed(uiState.studentTaskAttempts) { index, attempt ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp),
                                text = "${attempt.number}.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalAppTheme.current.text,
                                textAlign = TextAlign.Center
                            )
                            VerticalDivider(
                                color = LocalAppTheme.current.divider,
                                fillMaxHeight = 0.85f
                            )
                            Text(
                                modifier = Modifier
                                    .weight(4f)
                                    .padding(horizontal = 2.dp),
                                text = SimpleDateFormat("dd.MM.yyyy HH:mm").format(dateFormat.parse(attempt.attemptDate)),
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalAppTheme.current.text,
                                textAlign = TextAlign.Center
                            )
                            VerticalDivider(
                                color = LocalAppTheme.current.divider,
                                fillMaxHeight = 0.85f
                            )
                            Text(
                                modifier = Modifier
                                    .weight(3f)
                                    .padding(horizontal = 2.dp),
                                text = if (attempt.isSuccessful) "Success" else "Fail",
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalAppTheme.current.text,
                                textAlign = TextAlign.Center
                            )
                            VerticalDivider(
                                color = LocalAppTheme.current.divider,
                                fillMaxHeight = 0.85f
                            )
                            Text(
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(horizontal = 2.dp),
                                text = "${attempt.mark}/${uiState.task.maxMark}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalAppTheme.current.text,
                                textAlign = TextAlign.Center
                            )
                            VerticalDivider(
                                color = LocalAppTheme.current.divider,
                                fillMaxHeight = 0.85f
                            )
                            Text(
                                modifier = Modifier
                                    .weight(4f)
                                    .padding(horizontal = 2.dp),
                                text = if (uiState.authType is AuthType.Student) "You" else "${attempt.student.surname} ${attempt.student.name}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalAppTheme.current.text,
                                textAlign = TextAlign.Center
                            )
                        }
                        if (index != uiState.studentTaskAttempts.lastIndex) {
                            HorizontalDivider(
                                color = LocalAppTheme.current.divider,
                                fillMaxWidth = 0.95f
                            )
                        }
                    }
                }
            }
        }
        if (canEdit && uiState.showSaveChangesButton) {
            app.presenter.components.buttons.TextButton(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.BottomCenter),
                text = if (taskIsCreated) "Save Changes" else "Create Task",
                color = LocalAppTheme.current.text,
                backgroundColor = LocalAppTheme.current.container,
                onClick = {
                    onUiAction(TaskUiAction.SaveChanges)
                }
            )
        }
        if (!canEdit) {
            if (uiState.task.diagramJson.isBlank()) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp, end = 24.dp)
                        .align(Alignment.BottomEnd),
                    text = "No Diagram yet.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.text
                )
            } else if (uiState.studentTaskAttempts.size >= uiState.task.maxAttempts) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 24.dp, end = 24.dp)
                        .align(Alignment.BottomEnd),
                    text = "You have no attempts left.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.text
                )
            } else {
                app.presenter.components.buttons.TextButton(
                    modifier = Modifier
                        .padding(bottom = 24.dp, end = 24.dp)
                        .align(Alignment.BottomEnd),
                    text = "Submit Attempt",
                    color = LocalAppTheme.current.text,
                    backgroundColor = LocalAppTheme.current.container,
                    onClick = {
                        onUiAction(TaskUiAction.CreateDiagram)
                    }
                )
            }
        } else {
            app.presenter.components.buttons.TextButton(
                modifier = Modifier
                    .padding(bottom = 24.dp, end = 24.dp)
                    .align(Alignment.BottomEnd),
                text = if (uiState.task.diagramJson.length < 5) "Create Diagram" else "Update Diagram",
                color = LocalAppTheme.current.text,
                backgroundColor = LocalAppTheme.current.container,
                onClick = {
                    onUiAction(TaskUiAction.CreateDiagram)
                }
            )
        }
    }
}