package app.presenter.screens.designing.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.components.*
import app.domain.viewModels.designing.*
import app.presenter.components.common.*

@Composable
fun ClassComponentDetailsColumn(
    modifier: Modifier = Modifier,
    uiState: DesigningUiState,
    onUiAction: (DesigningUiAction) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        uiState.focusedComponentReference?.let { ref ->
            DefaultTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                startValue = ref.name,
                label = "Name:",
                onValueChange = { newValue ->
                    uiState.classComponents.last().name = newValue
                    onUiAction(DesigningUiAction.UpdateCommonCounter)
                },
                maxLength = 48,
                showEditIcon = false,
                textStyle = MaterialTheme.typography.bodyMedium,
                labelTextStyle = MaterialTheme.typography.bodySmall,
                textColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}