package app.domain.viewModels.task

sealed interface TaskUiAction {
    data object FetchData : TaskUiAction

    data object CreateDiagram: TaskUiAction
    data class UpdateDiagram(val diagramJson: String): TaskUiAction

    data class UpdateName(val name: String): TaskUiAction
    data class UpdateDescription(val description: String): TaskUiAction
    data class UpdateMaxAttempts(val maxAttempts: Int): TaskUiAction
    data object SaveChanges : TaskUiAction
}