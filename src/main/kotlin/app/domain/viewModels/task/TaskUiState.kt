package app.domain.viewModels.task

import app.domain.model.*

data class TaskUiState(
    val task: Task = Task(),
    val showSaveChangesButton: Boolean = false
)