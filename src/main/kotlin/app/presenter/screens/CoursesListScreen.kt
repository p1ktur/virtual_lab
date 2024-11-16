package app.presenter.screens

import androidx.compose.animation.core.*
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
import app.domain.auth.*
import app.domain.viewModels.courses.coursesList.*
import app.presenter.components.common.*
import app.presenter.theme.*

@Composable
fun CoursesListScreen(
    uiState: CoursesListUiState,
    onUiAction: (CoursesListUiAction) -> Unit
) {
    val canEdit = remember(uiState.authType) {
        uiState.authType is AuthType.Teacher || uiState.authType is AuthType.Administrator
    }

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
        Text(
            modifier = Modifier
                .padding(8.dp)
                .alpha(screenTitleAlpha)
                .align(Alignment.TopCenter),
            text = "Your Courses",
            style = MaterialTheme.typography.titleLarge,
            color = LocalAppTheme.current.text
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = if (canScroll) 16.dp else 8.dp),
            state = columnScrollState,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height((MaterialTheme.typography.titleLarge.fontSize.value + 24).dp))
            }
            if (uiState.courses.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No Courses Yet",
                        style = MaterialTheme.typography.titleSmall,
                        color = LocalAppTheme.current.text,
                        textAlign = TextAlign.Center
                    )
                }
            }
            itemsIndexed(uiState.courses, key = { _, it -> it.id }) { index, course ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onUiAction(CoursesListUiAction.OpenCourse(course.id))
                            }
                            .padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        if (canEdit) {
                            Spacer(modifier = Modifier.height(16.dp))
                        } else {
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                        Text(
                            text = course.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = LocalAppTheme.current.text
                        )
                        Text(
                            text = course.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalAppTheme.current.text
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    if (canEdit) {
                        app.presenter.components.buttons.IconButton(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.TopEnd),
                            icon = Icons.Filled.Delete,
                            actionText = "Delete Course",
                            color = LocalAppTheme.current.text,
                            backgroundColor = Color.Transparent,
                            shape = RectangleShape,
                            onClick = {
                                onUiAction(CoursesListUiAction.DeleteCourse(course.id))
                            }
                        )
                    }
                }
                if (index != uiState.courses.lastIndex) {
                    HorizontalDivider(
                        color = LocalAppTheme.current.divider,
                        fillMaxWidth = 0.95f
                    )
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
        if (canEdit) {
            Box(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                app.presenter.components.buttons.IconButton(
                    modifier = Modifier.size(32.dp),
                    icon = Icons.Filled.Add,
                    actionText = "Add Course",
                    color = LocalAppTheme.current.text,
                    backgroundColor = Color.Transparent,
                    shape = RectangleShape,
                    onClick = {
                        onUiAction(CoursesListUiAction.CreateCourse)
                    }
                )
            }
        }
    }
}