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
import app.domain.viewModels.courses.coursesList.*
import app.presenter.components.common.*
import app.presenter.theme.*

@Composable
fun CoursesListScreen(
    uiState: CoursesListUiState,
    onUiAction: (CoursesListUiAction) -> Unit
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

    val courses = listOf(
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
        Course(),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LocalAppTheme.current.primaryScreenTwo),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .alpha(screenTitleAlpha),
            text = "Your Courses",
            style = MaterialTheme.typography.titleLarge,
            color = LocalAppTheme.current.primaryScreenText
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 8.dp, end = if (canScroll) 8.dp else 0.dp),
            state = columnScrollState
        ) {
            item {
                Spacer(modifier = Modifier.height((MaterialTheme.typography.titleLarge.fontSize.value + 16).dp))
            }
            itemsIndexed(courses) { index, course ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        },
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = course.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = LocalAppTheme.current.primaryScreenText
                    )
                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.primaryScreenText
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                }
                if (index != courses.lastIndex) {
                    HorizontalDivider(
                        color = LocalAppTheme.current.primaryScreenDivider,
                        fillMaxWidth = 0.85f
                    )
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