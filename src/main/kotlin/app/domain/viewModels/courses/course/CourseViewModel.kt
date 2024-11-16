package app.domain.viewModels.courses.course

import app.data.server.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class CourseViewModel(
    private val courseId: Int?,
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourseUiState(serverRepository.authenticatedType.value))
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: CourseUiAction) {
        viewModelScope.launch {
            when (action) {
                CourseUiAction.FetchData -> fetchData()

                is CourseUiAction.OpenTask -> Unit
                CourseUiAction.CreateTask -> Unit
                is CourseUiAction.DeleteTask -> deleteTask(action.taskId)

                is CourseUiAction.CreateEducationalMaterial -> Unit
                is CourseUiAction.OpenEducationalMaterial -> Unit
                is CourseUiAction.DeleteEducationalMaterial -> deleteEducationMaterial(action.educationalMaterialId)

                is CourseUiAction.UpdateName -> updateName(action.name)
                is CourseUiAction.UpdateDescription -> updateDescription(action.description)

                is CourseUiAction.OpenStudentsList -> Unit
                is CourseUiAction.AddStudentToCourse -> addStudentToCourse(action.studentId)
                is CourseUiAction.RemoveStudentFromCourse -> removeStudentFromCourse(action.studentId)

                CourseUiAction.SaveChanges -> saveChanges()
                CourseUiAction.DeleteCourse -> deleteCourse()
            }
        }
    }

    private fun fetchData() {
        courseId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val course = serverRepository.getCourse(id)
                val tasks = serverRepository.getCourseTasks(id)
                val educationalMaterials = serverRepository.getCourseEducationalMaterials(id)

                if (course != null) {
                    _uiState.update {
                        it.copy(
                            course = course.copy(
                                educationalMaterials = educationalMaterials ?: emptyList()
                            )
                        )
                    }
                }

                if (tasks != null) {
                    _uiState.update {
                        it.copy(
                            tasks = tasks
                        )
                    }
                }
            }
        }
    }

    private fun deleteTask(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            serverRepository.deleteTask(taskId)
        }
    }

    private fun deleteEducationMaterial(educationalMaterialId: Int) {
        val index = uiState.value.course.educationalMaterials.map { it.id }.indexOf(educationalMaterialId)
        val toDeleteMaterial = uiState.value.course.educationalMaterials[index]

        _uiState.update {
            it.copy(
                course = it.course.copy(educationalMaterials = it.course.educationalMaterials.minus(setOf(toDeleteMaterial))),
                showSaveChangesButton = true
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            serverRepository.deleteEducationalMaterial(educationalMaterialId)
        }
    }

    private fun updateName(name: String) {
        _uiState.update {
            it.copy(
                course = it.course.copy(name = name),
                showSaveChangesButton = true
            )
        }
    }

    private fun updateDescription(description: String) {
        _uiState.update {
            it.copy(
                course = it.course.copy(description = description),
                showSaveChangesButton = true
            )
        }
    }

    private fun addStudentToCourse(studentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            serverRepository.addStudentToCourse(courseId ?: uiState.value.course.id, studentId)
        }
    }

    private fun removeStudentFromCourse(studentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            serverRepository.removeStudentFromCourse(courseId ?: uiState.value.course.id, studentId)
        }
    }

    private fun saveChanges() {
        _uiState.update {
            it.copy(
                showSaveChangesButton = false
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (courseId == null && uiState.value.course.id == 0) {
                val authId = serverRepository.getAuthId() ?: return@launch
                serverRepository.createCourse(authId, uiState.value.course)?.let { newId ->
                    _uiState.update {
                        it.copy(
                            course = it.course.copy(id = newId)
                        )
                    }
                }
            } else if (courseId != null) {
                serverRepository.updateCourse(courseId, uiState.value.course)
            } else {
                serverRepository.updateCourse(uiState.value.course.id, uiState.value.course)
            }
        }
    }

    private fun deleteCourse() {
        if (courseId == null) return

        viewModelScope.launch(Dispatchers.IO) {
            serverRepository.deleteCourse(courseId)
        }
    }
}