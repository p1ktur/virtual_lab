package app.domain.viewModels.studentsList

import app.data.server.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class StudentsListViewModel(
    private val courseId: Int?,
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StudentsListUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: StudentsListUiAction) {
        viewModelScope.launch {
            when (action) {
                is StudentsListUiAction.FetchData -> fetchData(action.inverse)
                is StudentsListUiAction.SelectStudent -> Unit
            }
        }
    }

    private fun fetchData(inverse: Boolean) {
        courseId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val studentsList = if (inverse) {
                    serverRepository.getStudentsList(id)
                } else {
                    serverRepository.getStudentsListFromCourse(id)
                }

                if (studentsList != null) {
                    _uiState.update {
                        it.copy(
                            studentsList = studentsList
                        )
                    }
                }
            }
        }
    }
}