package app.domain.viewModels.courses.coursesList

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class CoursesListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesListUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: CoursesListUiAction) {
        viewModelScope.launch {
            when (action) {
                else -> Unit
            }
        }
    }
}