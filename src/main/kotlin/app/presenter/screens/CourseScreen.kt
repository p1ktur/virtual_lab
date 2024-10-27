package app.presenter.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.unit.*
import app.domain.model.*
import app.domain.viewModels.courses.course.*
import app.presenter.components.common.*

@Composable
fun CourseScreen(
    uiState: CourseUiState,
    onUiAction: (CourseUiAction) -> Unit
) {
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

    val course = Course()

    val tasks = listOf(
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
        Task(),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .shadow(8.dp, RoundedCornerShape(12f))
            .clip(RoundedCornerShape(12f))
            .background(MaterialTheme.colorScheme.primaryContainer)
            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 16.dp)
                .alpha(screenTitleAlpha),
            text = "Tasks List",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
            ) {
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = course.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            VerticalDivider(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fillMaxHeight = 0.95f
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 8.dp, end = if (canScroll) 16.dp else 0.dp),
                state = columnScrollState
            ) {
                item {
                    Spacer(modifier = Modifier.height((MaterialTheme.typography.titleLarge.fontSize.value + 16).dp))
                }
                itemsIndexed(tasks) { index, task ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                            },
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = task.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    if (index != tasks.lastIndex) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fillMaxWidth = 0.85f
                            )
                        }
                    }
                }

            }
        }
        if (canScroll) {
            VerticalScrollbar(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(columnScrollState),
                style = ScrollbarStyle(
                    minimalHeight = 8.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4f),
                    hoverDurationMillis = 200,
                    unhoverColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    hoverColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}