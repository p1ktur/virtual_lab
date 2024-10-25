import app.data.fileManager.*
import app.domain.umlDiagram.mouse.*
import app.domain.viewModels.designing.*
import kotlinx.coroutines.*
import org.junit.Test
import java.io.*
import kotlin.test.*

class DesigningViewModelTest {

    @Test
    fun `Test How ViewModel loads Save Data`() = runBlocking {
        val designingViewModel = DesigningViewModel()
        val fileManager = designingViewModel.setupFileLoading()
        val uiState = designingViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        assertTrue(uiState.value.classConnections.isEmpty())

        fileManager.load(File("C:\\class_diagrams\\class_diagram_changed.json"))

        assertTrue(uiState.value.classComponents.isNotEmpty() || uiState.value.classConnections.isNotEmpty())
    }

    @Test
    fun `Test How ViewModel processes Component deleting`() = runBlocking {
        val designingViewModel = DesigningViewModel()
        val fileManager = designingViewModel.setupFileLoading()
        val uiState = designingViewModel.uiState

        fileManager.load(File("C:\\class_diagrams\\class_diagram_changed.json"))

        val toDeleteComponent = uiState.value.classComponents.first()
        val previousSize = uiState.value.classComponents.size

        val someConnectionsReferToToBeDeletedComponent = uiState.value.classConnections.any {
            it.startRef.getRefClass() == toDeleteComponent || it.endRef.getRefClass() == toDeleteComponent
        }

        assertTrue(someConnectionsReferToToBeDeletedComponent)

        designingViewModel.onUiAction(DesigningUiAction.DeleteComponent(0))
        delay(100)

        assertEquals(previousSize - 1, uiState.value.classComponents.size)

        val noConnectionsReferToDeletedComponent = uiState.value.classConnections.all {
            it.startRef.getRefClass() != toDeleteComponent && it.endRef.getRefClass() != toDeleteComponent
        }

        assertTrue(noConnectionsReferToDeletedComponent)
    }

    @Test
    fun `Test How ViewModel processes click on Component`() = runBlocking {
        val designingViewModel = DesigningViewModel()
        val uiState = designingViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        designingViewModel.onUiAction(DesigningUiAction.AddComponent)
        delay(100)

        assertEquals(1, uiState.value.classComponents.size)
        designingViewModel.onUiAction(DesigningUiAction.ClickOnComponent(0, ComponentContainmentResult.Whole))
        delay(100)

        assertEquals(uiState.value.classComponents.first(), uiState.value.focusUiState.focusedComponent?.first)
        assertTrue(uiState.value.componentInFocus)
    }

    @Test
    fun `Test How ViewModel creates New Connection`() = runBlocking {
        val designingViewModel = DesigningViewModel()
        val uiState = designingViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        designingViewModel.onUiAction(DesigningUiAction.AddComponent)
        delay(100)
        designingViewModel.onUiAction(DesigningUiAction.AddComponent)
        delay(100)
        assertEquals(2, uiState.value.classComponents.size)

        designingViewModel.onUiAction(DesigningUiAction.StartConnectionOn(0))
        delay(100)
        designingViewModel.onUiAction(DesigningUiAction.CreateConnectionOn(1))
        delay(100)

        assertEquals(1, uiState.value.classConnections.size)

        val createdConnectionRefersToItsComponentsCorrectly = uiState.value.classConnections.first().run {
            startRef.getRefClass() == uiState.value.classComponents.first() && endRef.getRefClass() == uiState.value.classComponents.last()
        }

        assertTrue(createdConnectionRefersToItsComponentsCorrectly)
    }

    @Test
    fun `Test How ViewModel processes click on Connection`() = runBlocking {
        val designingViewModel = DesigningViewModel()
        val uiState = designingViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        designingViewModel.onUiAction(DesigningUiAction.AddComponent)
        delay(100)
        designingViewModel.onUiAction(DesigningUiAction.AddComponent)
        delay(100)
        assertEquals(2, uiState.value.classComponents.size)

        designingViewModel.onUiAction(DesigningUiAction.StartConnectionOn(0))
        delay(100)
        designingViewModel.onUiAction(DesigningUiAction.CreateConnectionOn(1))
        delay(100)

        assertEquals(1, uiState.value.classConnections.size)

        designingViewModel.onUiAction(DesigningUiAction.ClickOnConnection(0, ConnectionContainmentResult.Whole))
        delay(100)

        assertEquals(uiState.value.classConnections.first(), uiState.value.focusUiState.focusedConnection?.first)
        assertTrue(uiState.value.connectionInFocus)
    }

    private fun DesigningViewModel.setupFileLoading(): FileManager {
        val fileManager = FileManager()

        fileManager.onRequestSaveData = {
            getSaveData()
        }
        fileManager.onDeliverSaveData = { saveData ->
            applySaveData(saveData)
        }

        return fileManager
    }
}