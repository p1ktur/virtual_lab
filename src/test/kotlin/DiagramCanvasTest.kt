import ComparerTest.Companion.MAX_MARK
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.test.*
import androidx.compose.ui.unit.*
import app.data.fileManager.*
import app.domain.di.*
import app.domain.umlDiagram.comparing.*
import app.domain.umlDiagram.editing.*
import app.domain.viewModels.designing.*
import app.presenter.screens.designing.*
import app.presenter.screens.designing.components.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import org.junit.Test
import org.koin.compose.*
import org.koin.core.context.*
import java.io.*
import kotlin.test.*

@OptIn(ExperimentalTestApi::class)
class DiagramCanvasTest {

    @Test
    fun `Test new Diagram appears on click`() = runComposeUiTest {
        val designingViewModel = DesigningViewModel()

        setContent {
            val uiState = designingViewModel.uiState.collectAsState()

            DesigningScreen(
                uiState = uiState.value,
                onUiAction = designingViewModel::onUiAction
            )
        }

        val uiState = designingViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        onNode(hasTestTag("Tools and Actions Bar")).assertExists()
        onNode(hasTestTag("Add Component Button")).assertExists()
        onNode(hasTestTag("Add Component Button")).performClick()
        assertEquals(1, uiState.value.classComponents.size)
    }

    @Test
    fun `Test how next Diagram appears on Click`() = runComposeUiTest {
        val designingViewModel = DesigningViewModel()

        setContent {
            val uiState = designingViewModel.uiState.collectAsState()

            DesigningScreen(
                uiState = uiState.value,
                onUiAction = designingViewModel::onUiAction
            )
        }

        val uiState = designingViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        onNode(hasTestTag("Tools and Actions Bar")).assertExists()
        onNode(hasTestTag("Add Component Button")).assertExists()
        onNode(hasTestTag("Add Component Button")).performClick()
        assertEquals(1, uiState.value.classComponents.size)

        onNode(hasTestTag("Add Component Button")).performClick()
        assertEquals(2, uiState.value.classComponents.size)

        assertEquals(16f, uiState.value.classComponents.last().position.x - uiState.value.classComponents.first().position.x)
        assertEquals(16f, uiState.value.classComponents.last().position.y - uiState.value.classComponents.first().position.y)

        onNode(hasTestTag("Connector Tool Button")).performClick()

        onNode(hasTestTag("Selector Tool Button")).performClick()
    }

    @Test
    fun `Test how Edit Mode Changes`() = runComposeUiTest {
        val designingViewModel = DesigningViewModel()

        setContent {
            val uiState = designingViewModel.uiState.collectAsState()

            DesigningScreen(
                uiState = uiState.value,
                onUiAction = designingViewModel::onUiAction
            )
        }

        val uiState = designingViewModel.uiState

        onNode(hasTestTag("Tools and Actions Bar")).assertExists()
        onNode(hasTestTag("Selector Tool Button")).assertExists()
        onNode(hasTestTag("Connector Tool Button")).assertExists()
        assertEquals(EditMode.SELECTOR, uiState.value.editMode)

        onNode(hasTestTag("Connector Tool Button")).performClick()
        assertEquals(EditMode.CONNECTOR, uiState.value.editMode)

        onNode(hasTestTag("Selector Tool Button")).performClick()
        assertEquals(EditMode.SELECTOR, uiState.value.editMode)
    }

//    fun failed() = runComposeUiTest {
//        var density = Density(1f)
//
//        setContent {
//            density = LocalDensity.current
//
//            val uiState = designingViewModel.uiState.collectAsState()
//
//            DesigningScreen(
//                uiState = uiState.value,
//                onUiAction = designingViewModel::onUiAction
//            )
//        }
//
//        val uiState = designingViewModel.uiState
//
//        assertTrue(uiState.value.classComponents.isEmpty())
//        onNode(hasTestTag("Tools and Actions Bar")).assertExists()
//        onNode(hasTestTag("Add Component Button")).assertExists()
//        onNode(hasTestTag("Add Component Button")).performClick()
//        assertEquals(1, uiState.value.classComponents.size)
//
//        val bounds = onNode(hasTestTag("Diagram Canvas")).getBoundsInRoot()
//        val size = with (density) { bounds.size.toSize() }
//
//        val centerPos = Offset(size.width / 2, size.height / 2)
//        val firstComponent = uiState.value.classComponents.first()
//
//        assertTrue(centerPos.x in firstComponent.position.x..firstComponent.position.x + firstComponent.size.width &&
//                centerPos.y in firstComponent.position.y..firstComponent.position.y + firstComponent.size.height)
//
//        onNode(hasTestTag("Diagram Canvas")).performMouseInput {
//            enter()
//            moveTo(Offset(size.width / 2, size.height / 2), 100)
//            press()
//            moveTo(Offset(size.width / 4, size.height / 4), 100)
//            release()
//            exit()
//        }
//
//        onNode(hasTestTag("Add Component Button")).performClick()
//        assertEquals(2, uiState.value.classComponents.size)
//        onNode(hasTestTag("Connector Tool Button")).performClick()
//
//        onNode(hasTestTag("Diagram Canvas")).performMouseInput {
//            enter()
//            moveTo(Offset(size.width / 2, size.height / 2), 100)
//            press()
//            release()
//            moveTo(Offset(size.width / 4, size.height / 4), 100)
//            press()
//            release()
//            exit()
//        }
//
//        onNode(hasTestTag("Selector Tool Button")).performClick()
//        assertEquals(1, uiState.value.classConnections.size)
//    }
}