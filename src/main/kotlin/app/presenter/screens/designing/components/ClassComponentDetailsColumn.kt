package app.presenter.screens.designing.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import app.domain.umlDiagram.model.components.*
import app.presenter.components.common.*

@Composable
fun ClassComponentDetailsColumn(
    modifier: Modifier = Modifier,
    classComponents: List<UMLClassComponent>,
    focusedDiagramReference: UMLClassComponent?,
    updateCounter: MutableState<Int>
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        focusedDiagramReference?.let { ref ->
            DefaultTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                startValue = ref.name,
                label = "Name:",
                onValueChange = { newValue ->
                    classComponents.last().name = newValue
                    updateCounter.value++
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