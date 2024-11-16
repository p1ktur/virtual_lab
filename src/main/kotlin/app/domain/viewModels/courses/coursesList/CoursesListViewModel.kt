package app.domain.viewModels.courses.coursesList

import app.data.server.*
import app.domain.auth.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class CoursesListViewModel(
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CoursesListUiState(serverRepository.authenticatedType.value))
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: CoursesListUiAction) {
        viewModelScope.launch {
            when (action) {
                CoursesListUiAction.FetchData -> fetchData()

                is CoursesListUiAction.OpenCourse -> Unit
                CoursesListUiAction.CreateCourse -> Unit
                is CoursesListUiAction.DeleteCourse -> deleteCourse(action.courseId)
            }
        }
    }

    private fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            val authType = serverRepository.authenticatedType.value
            val courses = when (authType) {
                is AuthType.Teacher -> serverRepository.getTeacherCourses(authType.user.id)
                is AuthType.Student -> serverRepository.getStudentCourses(authType.user.id)
                else -> return@launch
            }

            _uiState.update {
                it.copy(
                    authType = authType
                )
            }

            if (courses != null) {
                _uiState.update {
                    it.copy(
                        courses = courses
                    )
                }
            }
        }
    }

    private fun deleteCourse(courseId: Int) {
        val index = uiState.value.courses.map { it.id }.indexOf(courseId)
        val toDeleteCourse = uiState.value.courses[index]

        _uiState.update {
            it.copy(
                courses = it.courses.minus(setOf(toDeleteCourse))
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            serverRepository.deleteCourse(courseId)
        }
    }
}