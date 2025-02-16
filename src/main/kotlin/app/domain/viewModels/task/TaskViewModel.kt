package app.domain.viewModels.task

import app.data.fileManager.*
import app.data.server.*
import app.domain.auth.*
import app.domain.model.*
import app.domain.umlDiagram.comparing.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*
import java.net.*
import java.text.*

class TaskViewModel(
    private val courseId: Int,
    private val taskId: Int?,
    private val serverRepository: ServerRepository,
    private val umlDiagramComparer: UMLDiagramComparer
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState(serverRepository.authenticatedType.value))
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: TaskUiAction) {
        viewModelScope.launch {
            when (action) {
                TaskUiAction.FetchData -> fetchData()
                is TaskUiAction.SubmitAttempt -> submitAttempt(action.diagramJson)

                TaskUiAction.CreateDiagram -> Unit
                is TaskUiAction.UpdateDiagram -> updateDiagram(action.diagramJson)

                is TaskUiAction.UpdateName -> updateName(action.name)
                is TaskUiAction.UpdateDescription -> updateDescription(action.description)

                TaskUiAction.AddMaxAttempts -> addMaxAttempts()
                TaskUiAction.SubMaxAttempts -> subMaxAttempts()
                is TaskUiAction.UpdateMaxAttempts -> updateMaxAttempts(action.maxAttempts)

                is TaskUiAction.UpdatePassMark -> updatePassMark(action.passMark)
                is TaskUiAction.UpdateMaxMark -> updateMaxMark(action.maxMark)

                TaskUiAction.SaveChanges -> saveChanges()
                TaskUiAction.DeleteTask -> deleteTask()
            }
        }
    }

    private fun fetchData() {
        taskId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val task = serverRepository.getTask(id)
                val studentTaskAttempts = when (val authType = uiState.value.authType) {
                    is AuthType.Administrator -> serverRepository.getStudentAttemptsByTask(id)
                    is AuthType.Teacher -> serverRepository.getStudentAttemptsByTask(id)
                    is AuthType.Student -> serverRepository.getStudentAttemptsByTaskAndStudent(id, authType.user.id)
                } ?: emptyList()

                if (task != null) {
                    _uiState.update {
                        it.copy(
                            task = task,
                            studentTaskAttempts = studentTaskAttempts
                        )
                    }
                }
            }
        } ?: {
            _uiState.update {
                it.copy(
                    task = it.task.copy(courseId = courseId)
                )
            }
        }
    }

    private fun submitAttempt(diagramJson: String) {
        if (uiState.value.task.diagramJson.isEmpty()) return

        val authType = uiState.value.authType
        if (authType !is AuthType.Student) return

        val json = ServerJson.get()

        taskId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val decodedDiagramJson = URLDecoder.decode(uiState.value.task.diagramJson, "utf-8")
                val standardJson = json.decodeFromString<SaveData>(decodedDiagramJson).toCompareData()
                val toCompareJson = json.decodeFromString<SaveData>(diagramJson).toCompareData()
                val mark = withContext(Dispatchers.Default) {
                    umlDiagramComparer
                        .compare(standardJson, toCompareJson)
                        .value(uiState.value.task.maxMark)
                }

                val encodedDiagramJson = URLEncoder.encode(diagramJson, "utf-8")
                val attempt = StudentTaskAttempt(
                    id = 0,
                    studentId = authType.user.id,
                    student = authType.user,
                    taskId = id,
                    attemptDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").format(System.currentTimeMillis()),
                    isSuccessful = mark >= uiState.value.task.minMark,
                    number = 0,
                    mark = mark,
                    studentDiagramJson = encodedDiagramJson
                )

                serverRepository.submitStudentAttempt(attempt)?.let { submittedAttempt ->
                    _uiState.update {
                        it.copy(
                            studentTaskAttempts = it.studentTaskAttempts + submittedAttempt
                        )
                    }
                }
            }
        }
    }

    private fun updateDiagram(diagramJson: String) {
        val encodedDiagramJson = URLEncoder.encode(diagramJson, "utf-8")

        _uiState.update {
            it.copy(
                task = it.task.copy(diagramJson = encodedDiagramJson)
            )
        }

        saveChanges()
    }

    private fun updateName(name: String) {
        _uiState.update {
            it.copy(
                task = it.task.copy(name = name),
                showSaveChangesButton = true
            )
        }
    }

    private fun updateDescription(description: String) {
        _uiState.update {
            it.copy(
                task = it.task.copy(description = description),
                showSaveChangesButton = true
            )
        }
    }

    private fun addMaxAttempts() {
        if (uiState.value.task.maxAttempts >= 9999) return

        _uiState.update {
            it.copy(
                task = it.task.copy(maxAttempts = it.task.maxAttempts + 1),
                showSaveChangesButton = true
            )
        }
    }

    private fun subMaxAttempts() {
        if (uiState.value.task.maxAttempts <= 0) return

        _uiState.update {
            it.copy(
                task = it.task.copy(maxAttempts = it.task.maxAttempts - 1),
                showSaveChangesButton = true
            )
        }
    }

    private fun updateMaxAttempts(maxAttempts: Int) {
        _uiState.update {
            it.copy(
                task = it.task.copy(maxAttempts = maxAttempts),
                showSaveChangesButton = true
            )
        }
    }

    private fun updatePassMark(passMark: Int) {
        _uiState.update {
            it.copy(
                task = it.task.copy(minMark = passMark),
                showSaveChangesButton = true
            )
        }
    }

    private fun updateMaxMark(maxMark: Int) {
        _uiState.update {
            it.copy(
                task = it.task.copy(maxMark = maxMark),
                showSaveChangesButton = true
            )
        }
    }

    private fun saveChanges() {
        _uiState.update {
            it.copy(
                showSaveChangesButton = false
            )
        }

        val taskToSave = uiState.value.task

        viewModelScope.launch(Dispatchers.IO) {
            if (taskId == null && uiState.value.task.id == 0) {
                serverRepository.addTaskToCourse(courseId, taskToSave)?.let { newId ->
                    _uiState.update {
                        it.copy(
                            task = it.task.copy(id = newId)
                        )
                    }
                }
            } else if (taskId != null) {
                serverRepository.updateTask(taskId, taskToSave)
            } else {
                serverRepository.updateTask(uiState.value.task.id, taskToSave)
            }
        }
    }

    private fun deleteTask() {
        if (taskId == null) return

        viewModelScope.launch(Dispatchers.IO) {
            serverRepository.deleteTask(taskId)
        }
    }
}