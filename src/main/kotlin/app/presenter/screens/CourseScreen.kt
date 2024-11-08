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
import app.presenter.theme.*

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppTheme.current.primaryScreenTwo),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 16.dp)
                .alpha(screenTitleAlpha),
            text = "Tasks List",
            style = MaterialTheme.typography.titleLarge,
            color = LocalAppTheme.current.primaryScreenText
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
                    text = uiState.course.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = LocalAppTheme.current.primaryScreenText
                )
                Text(
                    text = uiState.course.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalAppTheme.current.primaryScreenText
                )
            }
            VerticalDivider(
                color = LocalAppTheme.current.primaryScreenText,
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
                itemsIndexed(uiState.tasks) { index, task ->
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
                            color = LocalAppTheme.current.primaryScreenText
                        )
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = LocalAppTheme.current.primaryScreenText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    if (index != uiState.tasks.lastIndex) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            HorizontalDivider(
                                color = LocalAppTheme.current.primaryScreenDivider,
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
                    .width(12.dp)
                    .background(LocalAppTheme.current.primaryScreenTwo)
                    .padding(horizontal = 2.dp)
                    .align(Alignment.CenterEnd),
                adapter = rememberScrollbarAdapter(columnScrollState),
                style = ScrollbarStyle(
                    minimalHeight = 8.dp,
                    thickness = 8.dp,
                    shape = RoundedCornerShape(4f),
                    hoverDurationMillis = 200,
                    unhoverColor = LocalAppTheme.current.primaryScreenTextDimmed,
                    hoverColor = LocalAppTheme.current.primaryScreenTextDimmedInverse
                )
            )
        }
    }
}