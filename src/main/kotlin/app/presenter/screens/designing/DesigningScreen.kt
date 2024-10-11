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
import app.presenter.screens.designing.components.*
import kotlin.random.*

const val ICON_BACKGROUND_COLOR = 0xFFDDEEDD
const val ICON_BACKGROUND_COLOR_HIGHLIGHTED = 0xFFDDDDFF
const val ICON_IMAGE_COLOR = 0xFF8899DD
const val ICON_IMAGE_COLOR_HIGHLIGHTED = 0xFFAAAAFF

@Composable
fun DesigningScreen() {
    // Global
    val updateCounter = remember { mutableIntStateOf(0) }

    // Diagram data
    val classComponents = remember {
        List(5) {
            UMLClassComponent(
                name = "Class $it",
                position = Offset(Random.nextFloat() * 1000f, Random.nextFloat() * 500f)
            )
        }.toMutableStateList()
    }

    val classConnections = remember { emptyList<UMLClassConnection>().toMutableStateList() }

    val diagramInFocus = remember { mutableStateOf(false) }
    val diagramSideInFocus = remember { mutableStateOf<SideDirection?>(null) }
    val diagramVertexInFocus = remember { mutableStateOf<VertexDirection?>(null) }
    val focusedDiagramReference = remember { mutableStateOf<UMLClassComponent?>(null) }

    // Editing data
    val editMode = remember { mutableStateOf(EditMode.SELECTOR) }

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
                classComponents = classComponents,
                focusedDiagramReference = focusedDiagramReference.value,
                updateCounter = updateCounter
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EditModesRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12f))
                        .background(Color.White)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f))
                        .padding(4.dp),
                    editMode = editMode
                )
                DiagramCanvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(12f))
                        .background(Color.White)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(12f)),
                    classComponents = classComponents,
                    classConnections = classConnections,
                    diagramInFocus = diagramInFocus,
                    diagramSideInFocus = diagramSideInFocus,
                    diagramVertexInFocus = diagramVertexInFocus,
                    focusedDiagramReference = focusedDiagramReference,
                    editMode = editMode,
                    updateCounter = updateCounter
                )
            }
        }
    }
}