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
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.domain.viewModels.studentsList.*
import app.presenter.components.common.*
import app.presenter.theme.*

@Composable
fun StudentsListScreen(
    uiState: StudentsListUiState,
    onUiAction: (StudentsListUiAction) -> Unit
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
            .background(LocalAppTheme.current.screenTwo),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier
                .padding(8.dp)
                .alpha(screenTitleAlpha)
                .align(Alignment.TopCenter),
            text = "Students",
            style = MaterialTheme.typography.titleLarge,
            color = LocalAppTheme.current.text
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, end = if (canScroll) 8.dp else 0.dp),
            state = columnScrollState
        ) {
            item {
                Spacer(modifier = Modifier.height((MaterialTheme.typography.titleLarge.fontSize.value + 24).dp))
            }
            if (uiState.studentsList.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "No Students Present",
                        style = MaterialTheme.typography.titleSmall,
                        color = LocalAppTheme.current.text,
                        textAlign = TextAlign.Center
                    )
                }
            }
            itemsIndexed(uiState.studentsList, key = { _, it -> it.id }) { index, student ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onUiAction(StudentsListUiAction.SelectStudent(student.id))
                        }
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = student.name,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = student.middleName,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = student.surname,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = student.birthDate,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text
                    )
                    Text(
                        modifier = Modifier.weight(1f),
                        text = student.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = LocalAppTheme.current.text
                    )
                }
                if (index != uiState.studentsList.lastIndex) {
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
    }
}