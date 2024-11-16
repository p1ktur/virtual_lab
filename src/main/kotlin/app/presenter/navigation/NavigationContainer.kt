package app.presenter.navigation

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import app.data.fileManager.*
import app.data.server.*
import app.domain.auth.*
import app.domain.actionTab.*
import app.domain.actionTab.options.*
import app.domain.model.*
import app.domain.viewModels.courses.course.*
import app.domain.viewModels.courses.coursesList.*
import app.domain.viewModels.diagrams.classDiagram.*
import app.domain.viewModels.educationalMaterial.*
import app.domain.viewModels.studentsList.*
import app.domain.viewModels.task.*
import app.presenter.components.common.*
import app.presenter.components.dialog.*
import app.presenter.screens.*
import app.presenter.screens.classDiagram.*
import app.presenter.theme.*
import com.darkrockstudios.libraries.mpfilepicker.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import moe.tlaster.precompose.koin.*
import moe.tlaster.precompose.navigation.*
import org.koin.compose.*
import org.koin.core.parameter.*
import java.awt.SystemColor.*
import java.io.*
import java.net.*

@Composable
fun NavigationScreen() {
    val coroutineScope = rememberCoroutineScope()

    val serverRepository = koinInject<ServerRepository>()
    val authType by serverRepository.authenticatedType.collectAsState()
    val authenticating by serverRepository.authenticating.collectAsState()

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

    if (authenticating) {
        LoadingText()
        return
    }

    TabNavigator(
        navOptions = listOf(),
        actionOptions = listOf(
            TabActionOption(
                name = "Log as Teacher",
                action = { navController, param ->
                    (param as? User.UserRole)?.let { userRole ->
                        serverRepository.authenticateAs(userRole, coroutineScope)
                        navController.clearBackStack(COURSES_LIST)
                    }
                },
                param = User.UserRole.TEACHER
            ),
            TabActionOption(
                name = "Log as Student",
                action = { navController, param ->
                    (param as? User.UserRole)?.let { userRole ->
                        serverRepository.authenticateAs(userRole, coroutineScope)
                        navController.clearBackStack(COURSES_LIST)
                    }
                },
                param = User.UserRole.STUDENT
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
            initialRoute = COURSES_LIST
        ) {
            scene(route = COURSES_LIST) {
                val viewModel = koinViewModel<CoursesListViewModel>()
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(authType) {
                    viewModel.onUiAction(CoursesListUiAction.FetchData)
                }

                // TODO change behaviour depending on authType
                // Student -> view & submit attempt
                // Teacher -> update & create data

                CoursesListScreen(
                    uiState = uiState,
                    onUiAction = { action ->
                        when (action) {
                            is CoursesListUiAction.OpenCourse -> navController.navigate("$COURSE/${action.courseId}")
                            CoursesListUiAction.CreateCourse -> navController.navigate("$COURSE/null")
                            else -> Unit
                        }
                        viewModel.onUiAction(action)
                    }
                )
            }
            scene(route = "$COURSE/{courseId}") { navBackStackEntry ->
                val courseId = navBackStackEntry.path<Int>("courseId")

                val viewModel = koinViewModel<CourseViewModel>(parameters = { parametersOf(courseId) })
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.onUiAction(CourseUiAction.FetchData)
                }

                // TODO change behaviour depending on authType
                // Student -> view & submit attempt
                // Teacher -> update & create data

                CourseScreen(
                    uiState = uiState,
                    onUiAction = { action ->
                        when (action) {
                            is CourseUiAction.OpenTask -> navController.navigate("$TASK/${courseId ?: uiState.course.id}/${action.taskId}")
                            CourseUiAction.CreateTask -> navController.navigate("$TASK/${courseId ?: uiState.course.id}/null")

                            CourseUiAction.CreateEducationalMaterial -> navController.navigate("$EDUCATIONAL_MATERIAL/${courseId ?: uiState.course.id}/null")
                            is CourseUiAction.OpenEducationalMaterial -> navController.navigate("$EDUCATIONAL_MATERIAL/${courseId ?: uiState.course.id}/${action.educationalMaterialId}")

                            is CourseUiAction.OpenStudentsList -> coroutineScope.launch {
                                val studentId = navController.navigateForResult("$STUDENTS_LIST/${courseId ?: uiState.course.id}/${action.inverse}") as? Int

                                if (studentId == null) return@launch

                                if (action.inverse) {
                                    viewModel.onUiAction(CourseUiAction.AddStudentToCourse(studentId))
                                } else {
                                    viewModel.onUiAction(CourseUiAction.RemoveStudentFromCourse(studentId))
                                }
                            }

                            CourseUiAction.DeleteCourse -> navController.goBack()
                            else -> Unit
                        }
                        viewModel.onUiAction(action)
                    }
                )
            }
            scene(route = "$TASK/{courseId}/{taskId}") { navBackStackEntry ->
                val courseId = navBackStackEntry.path<Int>("courseId")
                val taskId = navBackStackEntry.path<Int>("taskId")

                if (courseId == null) {
                    navController.goBack()
                    return@scene
                }

                val viewModel = koinViewModel<TaskViewModel>(parameters = { parametersOf(courseId, taskId) })
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.onUiAction(TaskUiAction.FetchData)
                }

                // TODO change behaviour depending on authType
                // Student -> view & submit attempt
                // Teacher -> update & create data
                // On submission view mark

                TaskScreen(
                    uiState = uiState,
                    onUiAction = { action ->
                        when (action) {
                            TaskUiAction.CreateDiagram -> coroutineScope.launch {
                                val diagramJson = navController.navigateForResult("$CLASS_DIAGRAM/${taskId ?: uiState.task.id}")?.toString() ?: ""
                                when (authType) {
                                    is AuthType.Teacher -> viewModel.onUiAction(TaskUiAction.UpdateDiagram(diagramJson))
                                    is AuthType.Student -> viewModel.onUiAction(TaskUiAction.SubmitAttempt(diagramJson))
                                    else -> Unit
                                }
                            }
                            is TaskUiAction.DeleteTask -> navController.goBack()
                            else -> Unit
                        }
                        viewModel.onUiAction(action)
                    }
                )
            }
            scene(route = "$CLASS_DIAGRAM/{taskId}") { navBackStackEntry ->
                val taskId = navBackStackEntry.path<Int>("taskId")

                val viewModel = koinViewModel<ClassDiagramViewModel>(parameters = { parametersOf(taskId) })
                val uiState by viewModel.uiState.collectAsState()

                // TODO change behaviour depending on authType
                // Student -> submit attempt
                // Teacher -> update diagram

                LaunchedEffect(Unit) {
                    fileManager.onRequestSaveData = {
                        viewModel.getSaveData()
                    }
                    fileManager.onDeliverSaveData = { saveData ->
                        viewModel.applySaveData(saveData)
                    }

                    viewModel.onUiAction(ClassDiagramUiAction.FetchData)
                }

                ClassDiagramScreen(
                    uiState = uiState,
                    onUiAction = { action ->
                        when (action) {
                            ClassDiagramUiAction.SaveChanges -> {
                                val saveData = viewModel.getSaveData()

                                navController.goBackWith(
                                    result = ServerJson.get().encodeToString(saveData)
                                )
                            }
                            else -> Unit
                        }
                        viewModel.onUiAction(action)
                    }
                )
            }
            scene(route = "$EDUCATIONAL_MATERIAL/{courseId}/{educationalMaterialId}") { navBackStackEntry ->
                val courseId = navBackStackEntry.path<Int>("courseId")
                val educationalMaterialId = navBackStackEntry.path<Int>("educationalMaterialId")

                if (courseId == null) {
                    navController.goBack()
                    return@scene
                }

                val viewModel = koinViewModel<EducationalMaterialViewModel>(parameters = { parametersOf(courseId, educationalMaterialId) })
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.onUiAction(EducationalMaterialUiAction.FetchData)
                }

                // TODO change behaviour depending on authType
                // Student -> view & submit attempt
                // Teacher -> update & create data

                EducationalMaterialScreen(
                    uiState = uiState,
                    onUiAction = { action ->
                        when (action) {
                            EducationalMaterialUiAction.DeleteEducationalMaterial -> navController.goBack()
                            else -> Unit
                        }
                        viewModel.onUiAction(action)
                    }
                )
            }
            scene(route = "$STUDENTS_LIST/{courseId}/{inverse}") { navBackStackEntry ->
                val courseId = navBackStackEntry.path<Int>("courseId")
                val inverse = navBackStackEntry.path<Boolean>("inverse") ?: true

                val viewModel = koinViewModel<StudentsListViewModel>(parameters = { parametersOf(courseId) })
                val uiState by viewModel.uiState.collectAsState()

                LaunchedEffect(Unit) {
                    viewModel.onUiAction(StudentsListUiAction.FetchData(inverse))
                }

                StudentsListScreen(
                    uiState = uiState,
                    onUiAction = { action ->
                        when (action) {
                            is StudentsListUiAction.SelectStudent -> navController.goBackWith(action.studentId)
                            else -> Unit
                        }
                        viewModel.onUiAction(action)
                    }
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