package app.domain.viewModels.task

import app.domain.auth.*
import app.domain.model.*

data class TaskUiState(
    val authType: AuthType,
    val task: Task = Task(),
    val showSaveChangesButton: Boolean = false
)