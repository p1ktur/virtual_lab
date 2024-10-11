package app.domain.util.result

sealed class TaskResult {
    data class Success<T>(val data: T) : TaskResult()
    data object Failure : TaskResult()
    data object NotCompleted : TaskResult()
}