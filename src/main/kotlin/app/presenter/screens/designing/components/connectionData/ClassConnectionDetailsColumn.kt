package app.presenter.screens.designing.components.connectionData

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.connection.*
import app.domain.viewModels.designing.*
import app.presenter.canvas.*
import app.presenter.canvas.ArrowHead.Companion.ARROW_HEAD_LENGTH
import app.presenter.components.common.*

@Composable
fun ClassConnectionDetailsColumn(
    modifier: Modifier = Modifier,
    reference: UMLClassConnection,
    commonCounter: Int,
    onUiAction: (DesigningUiAction) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DefaultTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            startValue = reference.name,
            label = "Name:",
            onValueChange = { newValue ->
                onUiAction(DesigningUiAction.UpdateConnectionData {
                    name = newValue
                })
            },
            maxLength = 48,
            showEditIcon = false,
            textStyle = MaterialTheme.typography.bodyMedium,
            labelTextStyle = MaterialTheme.typography.bodySmall,
            textColor = Color.Black
        )
        HorizontalDivider(
            color = Color.Black,
            fillMaxWidth = 1f
        )
        ConnectionCustomizationView(
            reference = reference,
            commonCounter = commonCounter,
            onUiAction = onUiAction
        )
    }
}