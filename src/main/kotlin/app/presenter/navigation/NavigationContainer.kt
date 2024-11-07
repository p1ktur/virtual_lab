package app.presenter.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import app.data.fileManager.*
import app.data.server.*
import app.domain.auth.*
import app.domain.actionTab.*
import app.domain.actionTab.options.*
import app.domain.viewModels.courses.course.*
import app.domain.viewModels.courses.coursesList.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.domain.viewModels.task.*
import app.presenter.components.common.*
import app.presenter.components.dialog.*
import app.presenter.screens.*
import app.presenter.screens.classDiagram.*
import com.darkrockstudios.libraries.mpfilepicker.*
import kotlinx.coroutines.*
import moe.tlaster.precompose.koin.*
import moe.tlaster.precompose.navigation.*
import org.koin.compose.*
import java.io.*

@Composable
fun NavigationScreen() {
    val coroutineScope = rememberCoroutineScope()

    val serverRepository = koinInject<ServerRepository>()
    val fileManager = koinInject<FileManager>()

    val openedSaveFile by remember { fileManager.openedSaveFile }

    var filePickerRequest by remember { mutableStateOf<FilePickerRequest?>(null) }
    var filePickerResult by remember { mutableStateOf<FilePickerResult?>(null) }

    var errorDialogText by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }

    LaunchedEffect(filePickerResult) {
        filePickerResult?.let { result ->
            when (result.action) {
                FileManager.Action.SAVE -> Unit
                FileManager.Action.SAVE_TO -> fileManager.saveTo(result.file)
                FileManager.Action.LOAD -> fileManager.load(result.file)
            }

            filePickerResult = null
        }
    }

    TabNavigator(
        navOptions = listOf(),
        actionOptions = listOf(
            TabActionOption(
                name = "Log as Teacher",
                action = { param ->
                    (param as? AuthType)?.let { authType ->
                        serverRepository.authenticateAs(authType)
                    }
                },
                param = AuthType.TEACHER
            ),
            TabActionOption(
                name = "Log as Student",
                action = { param ->
                    (param as? AuthType)?.let { authType ->
                        serverRepository.authenticateAs(authType)
                    }
                },
                param = AuthType.STUDENT
            )
        ),
        menuOptions = listOf(
            MenuOption(
                text = "Save",
                enabled = derivedStateOf { openedSaveFile != null },
                associatedRoutes = listOf(CLASS_DIAGRAM),
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        fileManager.save()
                    }
                }
            ),
            MenuOption(
                text = "Save to",
                enabled = mutableStateOf(true),
                associatedRoutes = listOf(CLASS_DIAGRAM),
                onClick = {
                    filePickerRequest = FilePickerRequest(
                        action = FileManager.Action.SAVE_TO,
                        extensions = FileManager.saveExtensions + ""
                    )
                }
            ),
            MenuOption(
                text = "Load",
                enabled = mutableStateOf(true),
                associatedRoutes = listOf(CLASS_DIAGRAM),
                onClick = {
                    filePickerRequest = FilePickerRequest(
                        action = FileManager.Action.LOAD,
                        extensions = FileManager.saveExtensions
                    )
                }
            )
        ),
        isLoading = false,
        navigationAllowed = true
    ) { navController ->
        NavHost(
            modifier = Modifier.fillMaxSize(),
            navigator = navController.navigator,
            initialRoute = CLASS_DIAGRAM
        ) {
            scene(route = COURSES_LIST) {
                val viewModel = koinViewModel<CoursesListViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                CoursesListScreen(
                    uiState = uiState,
                    onUiAction = viewModel::onUiAction,
                )
            }
            scene(route = COURSE) {
                val viewModel = koinViewModel<CourseViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                CourseScreen(
                    uiState = uiState,
                    onUiAction = viewModel::onUiAction,
                )
            }
            scene(route = TASK) {
                val viewModel = koinViewModel<TaskViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                TaskScreen(
                    uiState = uiState,
                    onUiAction = viewModel::onUiAction,
                )
            }
            scene(route = CLASS_DIAGRAM) {
                val viewModel = koinViewModel<ClassDiagramViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    fileManager.onRequestSaveData = {
                        viewModel.getSaveData()
                    }
                    fileManager.onDeliverSaveData = { saveData ->
                        viewModel.applySaveData(saveData)
                    }
                }

                ClassDiagramScreen(
                    uiState = uiState,
                    onUiAction = viewModel::onUiAction,
                )
            }
        }
    }

    FilePicker(
        show = filePickerRequest != null,
        initialDirectory = "C://",
        title = "Choose file",
        fileExtensions = filePickerRequest?.extensions?.toList() ?: emptyList()
    ) { mpFile ->
        val action = filePickerRequest?.action ?: return@FilePicker
        val extensions = filePickerRequest?.extensions ?: return@FilePicker
        filePickerRequest = null

        mpFile?.let {
            val file = File(mpFile.path)

            if (extensions.contains(file.extension)) {
                filePickerResult = FilePickerResult(action, file)
            } else {
                errorDialogText = "Please, select directory, .txt or .json file."
                showErrorDialog = true
            }
        }
    }

    ErrorDialog(
        size = DpSize(300.dp, 150.dp),
        title = "Error",
        text = errorDialogText,
        visible = showErrorDialog,
        onClose = { showErrorDialog = false }
    )
}