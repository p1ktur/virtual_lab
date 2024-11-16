package app.presenter.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
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
import app.domain.auth.*
import app.domain.viewModels.courses.course.*
import app.presenter.components.common.*
import app.presenter.theme.*

@Composable
fun CourseScreen(
    uiState: CourseUiState,
    onUiAction: (CourseUiAction) -> Unit
) {
    val canEdit = remember(uiState.authType) {
        uiState.authType is AuthType.Teacher || uiState.authType is AuthType.Administrator
    }
    val courseIsCreated = remember(uiState.course) { uiState.course.id > 0 }

    val columnScrollState = rememberLazyListState()
    val canScroll by remember {
        derivedStateOf {
            columnScrollState.canScrollForward || columnScrollState.canScrollBackward
        }
    }

    val screenTitleAlpha by animateFloatAsState(
        targetValue = if (columnScrollState.canScrollBackward) {
            0f
        } else {
            1f
        },
        animationSpec = tween(
            durationMillis = 300,
            easing = EaseInOut
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppTheme.current.screenTwo),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    if (canEdit && courseIsCreated) {
                        app.presenter.components.buttons.IconButton(
                            modifier = Modifier.size(28.dp),
                            icon = Icons.Filled.Delete,
                            actionText = "Delete Course",
                            color = LocalAppTheme.current.text,
                            backgroundColor = Color.Transparent,
                            shape = RectangleShape,
                            onClick = {
                                onUiAction(CourseUiAction.DeleteCourse)
                            }
                        )
                    }
                }
                if (canEdit) {
                    SingleLineTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp),
                        text = uiState.course.name,
                        label = "Name:",
                        onValueChange = { newValue ->
                            onUiAction(CourseUiAction.UpdateName(newValue))
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
                        text = "Name: ${uiState.course.name}",
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
                        text = uiState.course.description,
                        label = "Description:",
                        placeholder = "Type description:",
                        onValueChange = { newValue ->
                            onUiAction(CourseUiAction.UpdateDescription(newValue))
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
                        text = "Description: ${uiState.course.description}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalAppTheme.current.text
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(
                    color = LocalAppTheme.current.text,
                    fillMaxWidth = 1f
                )
                if (canEdit && courseIsCreated) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            text = "Students",
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalAppTheme.current.text
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        app.presenter.components.buttons.IconButton(
                            modifier = Modifier.size(28.dp),
                            icon = Icons.Filled.Remove,
                            actionText = "Remove Student From Course",
                            color = LocalAppTheme.current.text,
                            backgroundColor = Color.Transparent,
                            shape = RectangleShape,
                            onClick = {
                                onUiAction(CourseUiAction.OpenStudentsList(true))
                            }
                        )
                        app.presenter.components.buttons.IconButton(
                            modifier = Modifier.size(28.dp),
                            icon = Icons.Filled.Add,
                            actionText = "Add Student To Course",
                            color = LocalAppTheme.current.text,
                            backgroundColor = Color.Transparent,
                            shape = RectangleShape,
                            onClick = {
                                onUiAction(CourseUiAction.OpenStudentsList(false))
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        color = LocalAppTheme.current.text,
                        fillMaxWidth = 1f
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        text = "Educational Materials",
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalAppTheme.current.text
                    )
                    if (canEdit && courseIsCreated) {
                        app.presenter.components.buttons.IconButton(
                            modifier = Modifier.size(28.dp),
                            icon = Icons.Filled.AttachFile,
                            actionText = "Add Educational Material",
                            color = LocalAppTheme.current.text,
                            backgroundColor = Color.Transparent,
                            shape = RectangleShape,
                            onClick = {
                                onUiAction(CourseUiAction.CreateEducationalMaterial)
                            }
                        )
                    }
                }
                if (uiState.course.educationalMaterials.isEmpty()) {
                    Text(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp, vertical = 6.dp),
                        text = "No Materials Yet",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text
                    )
                } else {
                    LazyVerticalGrid(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        columns = GridCells.Fixed(4),
                        contentPadding = PaddingValues(6.dp)
                    ) {
                        items(uiState.course.educationalMaterials) { material ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onUiAction(CourseUiAction.OpenEducationalMaterial(material.id))
                                        }
                                        .padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .padding(8.dp),
                                        imageVector = Icons.Default.Description,
                                        contentDescription = "Educational Material Icon",
                                        tint = LocalAppTheme.current.text
                                    )
                                    Text(
                                        modifier = Modifier.padding(start = 4.dp, bottom = 4.dp, end = 4.dp),
                                        text = material.name,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = LocalAppTheme.current.text,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 1
                                    )
                                }
                                if (canEdit && courseIsCreated) {
                                    app.presenter.components.buttons.IconButton(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .align(Alignment.TopEnd),
                                        icon = Icons.Filled.Delete,
                                        actionText = "Delete Educational Material",
                                        color = LocalAppTheme.current.text,
                                        backgroundColor = Color.Transparent,
                                        shape = RectangleShape,
                                        onClick = {
                                            onUiAction(CourseUiAction.DeleteEducationalMaterial(material.id))
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
            VerticalDivider(
                color = LocalAppTheme.current.text,
                fillMaxHeight = 0.95f
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 8.dp, end = if (canScroll) 16.dp else 8.dp),
                state = columnScrollState
            ) {
                item {
                    Spacer(modifier = Modifier.height((MaterialTheme.typography.titleLarge.fontSize.value + 16).dp))
                }
                if (uiState.tasks.isEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "No Tasks Yet",
                            style = MaterialTheme.typography.titleSmall,
                            color = LocalAppTheme.current.text,
                            textAlign = TextAlign.Center
                        )
                    }
                }
                itemsIndexed(uiState.tasks, key = { _, it -> it.id }) { index, task ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onUiAction(CourseUiAction.OpenTask(task.id))
                                }
                                .padding(horizontal = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            if (canEdit) {
                                Spacer(modifier = Modifier.height(16.dp))
                            } else {
                                Spacer(modifier = Modifier.height(6.dp))
                            }
                            Text(
                                text = task.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = LocalAppTheme.current.text
                            )
                            Text(
                                text = task.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LocalAppTheme.current.text
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        if (canEdit && courseIsCreated) {
                            app.presenter.components.buttons.IconButton(
                                modifier = Modifier
                                    .size(24.dp)
                                    .align(Alignment.TopEnd),
                                icon = Icons.Filled.Delete,
                                actionText = "Delete Task",
                                color = LocalAppTheme.current.text,
                                backgroundColor = Color.Transparent,
                                shape = RectangleShape,
                                onClick = {
                                    onUiAction(CourseUiAction.DeleteTask(task.id))
                                }
                            )
                        }
                    }
                    if (index != uiState.tasks.lastIndex) {
                        HorizontalDivider(
                            color = LocalAppTheme.current.divider,
                            fillMaxWidth = 1f,
                            horizontalPadding = 8.dp
                        )
                    }
                }
            }
        }
        if (canScroll) {
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 32.dp)
                    .width(12.dp)
                    .background(LocalAppTheme.current.screenTwo)
                    .padding(horizontal = 2.dp)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(columnScrollState),
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
        Row(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .align(Alignment.TopEnd),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(start = 16.dp)
                    .alpha(screenTitleAlpha),
                text = "Tasks List",
                style = MaterialTheme.typography.titleMedium,
                color = LocalAppTheme.current.text,
                textAlign = TextAlign.Center
            )
            if (canEdit && courseIsCreated) {
                app.presenter.components.buttons.IconButton(
                    modifier = Modifier.size(28.dp),
                    icon = Icons.Filled.Add,
                    actionText = "Add Task",
                    color = LocalAppTheme.current.text,
                    backgroundColor = Color.Transparent,
                    shape = RectangleShape,
                    onClick = {
                        onUiAction(CourseUiAction.CreateTask)
                    }
                )
            }
        }
        if (canEdit && uiState.showSaveChangesButton) {
            app.presenter.components.buttons.TextButton(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.BottomCenter),
                text = if (courseIsCreated) "Save Changes" else "Create Course",
                color = LocalAppTheme.current.text,
                backgroundColor = LocalAppTheme.current.container,
                onClick = {
                    onUiAction(CourseUiAction.SaveChanges)
                }
            )
        }
    }
}