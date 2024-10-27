package app.domain.viewModels.courses.course

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class CourseViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: CourseUiAction) {
        viewModelScope.launch {
            when (action) {
                else -> Unit
            }
        }
    }
}