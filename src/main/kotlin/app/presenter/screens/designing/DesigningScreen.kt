package app.presenter.screens.designing

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.editing.*
import app.domain.umlDiagram.model.components.*
import app.domain.umlDiagram.model.connections.*
import app.domain.umlDiagram.mouse.*
import app.domain.util.geometry.*
import app.presenter.screens.designing.components.*
import kotlin.random.*
import app.domain.viewModels.designing.*

const val ICON_BACKGROUND_COLOR = 0xFFDDEEDD
const val ICON_BACKGROUND_COLOR_HIGHLIGHTED = 0xFFDDDDFF
const val EDIT_ICON_IMAGE_COLOR = 0xFF8899DD
const val EDIT_ICON_IMAGE_COLOR_HIGHLIGHTED = 0xFFAAAAFF
const val ACTION_ICON_IMAGE_COLOR = 0xFFDD9988
const val ACTION_ICON_IMAGE_COLOR_HIGHLIGHTED = 0xFFFFAAAA

@Composable
fun DesigningScreen(
    uiState: DesigningUiState,
    onUiAction: (DesigningUiAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ClassComponentDetailsColumn(
                modifier = Modifier
                    .fillMaxWidth(0.2f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12f))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                    .padding(4.dp),
                uiState = uiState,
                onUiAction = onUiAction
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ToolAndActionsBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12f))
                        .background(Color.White)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                        .padding(4.dp),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
                DiagramCanvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12f))
                        .background(Color.White)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f)),
                    uiState = uiState,
                    onUiAction = onUiAction
                )
            }
        }
    }
}