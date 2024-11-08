package app.domain.viewModels.task

import app.data.server.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class TaskViewModel(
    private val courseId: Int,
    private val taskId: Int?,
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: TaskUiAction) {
        viewModelScope.launch {
            when (action) {
                TaskUiAction.FetchData -> fetchData()
                TaskUiAction.CreateDiagram -> Unit
                is TaskUiAction.UpdateDiagram -> updateDiagram(action.diagramJson)
                is TaskUiAction.UpdateName -> updateName(action.name)
                is TaskUiAction.UpdateDescription -> updateDescription(action.description)
                is TaskUiAction.UpdateMaxAttempts -> updateMaxAttempts(action.maxAttempts)
                TaskUiAction.SaveChanges -> saveChanges()
            }
        }
    }

    private fun fetchData() {
        taskId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val task = serverRepository.getTask(id)

                _uiState.update {
                    it.copy(
                        task = task
                    )
                }
            }
        }
    }

    private fun updateDiagram(diagramJson: String) {
        _uiState.update {
            it.copy(
                task = it.task.copy(diagramJson = diagramJson),
                showSaveChangesButton = true
            )
        }
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

    private fun updateMaxAttempts(maxAttempts: Int) {
        _uiState.update {
            it.copy(
                task = it.task.copy(maxAttempts = maxAttempts),
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

        viewModelScope.launch(Dispatchers.IO) {
            if (taskId == null) {
                serverRepository.addTaskToCourse(courseId, uiState.value.task)
            } else {
                serverRepository.updateTask(taskId, uiState.value.task)
            }
        }
    }
}