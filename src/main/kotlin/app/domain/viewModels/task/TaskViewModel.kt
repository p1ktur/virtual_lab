package app.domain.viewModels.task

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class TaskViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: TaskUiAction) {
        viewModelScope.launch {
            when (action) {
                else -> Unit
            }
        }
    }
}