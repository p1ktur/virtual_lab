import app.data.dataSources.*
import app.data.fileManager.*
import app.data.server.*
import app.domain.umlDiagram.mouse.*
import app.domain.viewModels.diagrams.classDiagram.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.*
import org.junit.Test
import java.io.*
import kotlin.test.*

class ClassDiagramViewModelTest {

    private val httpClient = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(ContentNegotiation) {
            ServerJson.get()
        }
        install(DefaultRequest) {
            url(ServerRepository.BASE_URL)
        }
    }

    private val serverRepository = ServerRepository(CoursesDataSource(httpClient))

    @Test
    fun `Test How ViewModel loads Save Data`() = runBlocking {
        val classDiagramViewModel = ClassDiagramViewModel(null, serverRepository)
        val fileManager = classDiagramViewModel.setupFileLoading()
        val uiState = classDiagramViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        assertTrue(uiState.value.classConnections.isEmpty())

        fileManager.load(File("C:\\class_diagrams\\class_diagram_changed.json"))

        assertTrue(uiState.value.classComponents.isNotEmpty() || uiState.value.classConnections.isNotEmpty())
    }

    @Test
    fun `Test How ViewModel processes Component deleting`() = runBlocking {
        val classDiagramViewModel = ClassDiagramViewModel(null, serverRepository)
        val fileManager = classDiagramViewModel.setupFileLoading()
        val uiState = classDiagramViewModel.uiState

        fileManager.load(File("C:\\class_diagrams\\class_diagram_changed.json"))

        val toDeleteComponent = uiState.value.classComponents.first()
        val previousSize = uiState.value.classComponents.size

        val someConnectionsReferToToBeDeletedComponent = uiState.value.classConnections.any {
            it.startRef.getRefClass() == toDeleteComponent || it.endRef.getRefClass() == toDeleteComponent
        }

        assertTrue(someConnectionsReferToToBeDeletedComponent)

        classDiagramViewModel.onUiAction(ClassDiagramUiAction.DeleteComponent(0))
        delay(100)

        assertEquals(previousSize - 1, uiState.value.classComponents.size)

        val noConnectionsReferToDeletedComponent = uiState.value.classConnections.all {
            it.startRef.getRefClass() != toDeleteComponent && it.endRef.getRefClass() != toDeleteComponent
        }

        assertTrue(noConnectionsReferToDeletedComponent)
    }

    @Test
    fun `Test How ViewModel processes click on Component`() = runBlocking {
        val classDiagramViewModel = ClassDiagramViewModel(null, serverRepository)
        val uiState = classDiagramViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        classDiagramViewModel.onUiAction(ClassDiagramUiAction.AddComponent)
        delay(100)

        assertEquals(1, uiState.value.classComponents.size)
        classDiagramViewModel.onUiAction(ClassDiagramUiAction.ClickOnComponent(0, ComponentContainmentResult.Whole))
        delay(100)

        assertEquals(uiState.value.classComponents.first(), uiState.value.focusUiState.focusedComponent?.first)
        assertTrue(uiState.value.componentInFocus)
    }

    @Test
    fun `Test How ViewModel creates New Connection`() = runBlocking {
        val classDiagramViewModel = ClassDiagramViewModel(null, serverRepository)
        val uiState = classDiagramViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        classDiagramViewModel.onUiAction(ClassDiagramUiAction.AddComponent)
        delay(100)
        classDiagramViewModel.onUiAction(ClassDiagramUiAction.AddComponent)
        delay(100)
        assertEquals(2, uiState.value.classComponents.size)

        classDiagramViewModel.onUiAction(ClassDiagramUiAction.StartConnectionOn(0))
        delay(100)
        classDiagramViewModel.onUiAction(ClassDiagramUiAction.CreateConnectionOn(1))
        delay(100)

        assertEquals(1, uiState.value.classConnections.size)

        val createdConnectionRefersToItsComponentsCorrectly = uiState.value.classConnections.first().run {
            startRef.getRefClass() == uiState.value.classComponents.first() && endRef.getRefClass() == uiState.value.classComponents.last()
        }

        assertTrue(createdConnectionRefersToItsComponentsCorrectly)
    }

    @Test
    fun `Test How ViewModel processes click on Connection`() = runBlocking {
        val classDiagramViewModel = ClassDiagramViewModel(null, serverRepository)
        val uiState = classDiagramViewModel.uiState

        assertTrue(uiState.value.classComponents.isEmpty())
        classDiagramViewModel.onUiAction(ClassDiagramUiAction.AddComponent)
        delay(100)
        classDiagramViewModel.onUiAction(ClassDiagramUiAction.AddComponent)
        delay(100)
        assertEquals(2, uiState.value.classComponents.size)

        classDiagramViewModel.onUiAction(ClassDiagramUiAction.StartConnectionOn(0))
        delay(100)
        classDiagramViewModel.onUiAction(ClassDiagramUiAction.CreateConnectionOn(1))
        delay(100)

        assertEquals(1, uiState.value.classConnections.size)

        classDiagramViewModel.onUiAction(ClassDiagramUiAction.ClickOnConnection(0, ConnectionContainmentResult.Whole))
        delay(100)

        assertEquals(uiState.value.classConnections.first(), uiState.value.focusUiState.focusedConnection?.first)
        assertTrue(uiState.value.connectionInFocus)
    }

    private fun ClassDiagramViewModel.setupFileLoading(): FileManager {
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