package app.domain.viewModels.courses.coursesList

import app.data.server.*
import app.domain.auth.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class CoursesListViewModel(
    private val authType: AuthType,
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesListUiState(authType))
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: CoursesListUiAction) {
        viewModelScope.launch {
            when (action) {
                CoursesListUiAction.FetchData -> fetchData()
                is CoursesListUiAction.OpenCourse -> Unit
                CoursesListUiAction.CreateCourse -> Unit
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            val courses = when (authType) {
                AuthType.TEACHER -> serverRepository.getTeacherCourses()
                AuthType.STUDENT -> serverRepository.getStudentCourses()
            }

            _uiState.update {
                it.copy(
                    courses = courses
                )
            }
        }
    }
}