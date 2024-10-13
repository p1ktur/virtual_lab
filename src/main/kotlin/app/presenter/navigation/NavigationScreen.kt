package app.presenter.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import app.domain.tabNavigator.*
import app.domain.umlDiagram.model.*
import app.domain.util.fileManager.*
import app.domain.viewModels.designing.*
import app.presenter.components.common.*
import app.presenter.components.dialog.*
import app.presenter.screens.designing.*
import com.darkrockstudios.libraries.mpfilepicker.*
import kotlinx.coroutines.*
import moe.tlaster.precompose.koin.*
import moe.tlaster.precompose.navigation.*
import java.io.*

@Composable
fun NavigationScreen() {
    val coroutineScope = rememberCoroutineScope()

    val fileManager = remember { FileManager() }
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
        navOptions = listOf(
//            TabNavOption(
//                name = "Welcome",
//                route = "/welcome"
//            ),
        ),
        menuOptions = listOf(
            MenuOption(
                text = "Save",
                enabled = derivedStateOf { openedSaveFile != null },
                onClick = {
                    coroutineScope.launch(Dispatchers.IO) {
                        fileManager.save()
                    }
                }
            ),
            MenuOption(
                text = "Save to",
                enabled = mutableStateOf(true),
                onClick = {
                    filePickerRequest = FilePickerRequest(
                        action = FileManager.Action.SAVE_TO,
                        extensions = UMLDiagramComponent.saveExtensions + ""
                    )
                }
            ),
            MenuOption(
                text = "Load",
                enabled = mutableStateOf(true),
                onClick = {
                    filePickerRequest = FilePickerRequest(
                        action = FileManager.Action.LOAD,
                        extensions = UMLDiagramComponent.saveExtensions
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
            initialRoute = "/designing"
        ) {
            scene(route = "/designing") {
                val viewModel = koinViewModel<DesigningViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(true) {
                    fileManager.onRequestSaveData = {
                        viewModel.getSaveData()
                    }
                    fileManager.onDeliverSaveData = { saveData ->
                        viewModel.applySaveData(saveData)
                    }
                }

                DesigningScreen(
                    uiState = uiState,
                    onUiAction = viewModel::onUiAction,
                )
            }
        }
    }

    FilePicker(
        show = filePickerRequest != null,
        initialDirectory = "C://",
        title = "Choose folder to export data",
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