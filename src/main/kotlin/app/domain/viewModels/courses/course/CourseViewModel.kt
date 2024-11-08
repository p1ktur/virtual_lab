package app.domain.viewModels.courses.course

import app.data.server.*
import app.domain.auth.*
import app.domain.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class CourseViewModel(
    private val courseId: Int?,
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CourseUiState())
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: CourseUiAction) {
        viewModelScope.launch {
            when (action) {
                CourseUiAction.FetchData -> fetchData()
                is CourseUiAction.OpenTask -> Unit
                CourseUiAction.CreateTask -> Unit
                is CourseUiAction.UpdateName -> updateName(action.name)
                is CourseUiAction.UpdateDescription -> updateDescription(action.description)
                is CourseUiAction.AddEducationMaterial -> addEducationMaterial(action.material)
                is CourseUiAction.RemoveEducationMaterial -> removeEducationMaterial(action.index)
                CourseUiAction.SaveChanges -> saveChanges()
            }
        }
    }

    private fun fetchData() {
        courseId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val course = serverRepository.getCourse(id)
                val tasks = serverRepository.getCourseTasks(id)

                _uiState.update {
                    it.copy(
                        course = course,
                        tasks = tasks
                    )
                }
            }
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

    private fun addEducationMaterial(material: EducationalMaterial) {
        _uiState.update {
            it.copy(
                course = it.course.copy(educationalMaterials = it.course.educationalMaterials + material),
                showSaveChangesButton = true
            )
        }
    }

    private fun removeEducationMaterial(index: Int) {
        val toDeleteMaterial = uiState.value.course.educationalMaterials[index]

        _uiState.update {
            it.copy(
                course = it.course.copy(educationalMaterials = it.course.educationalMaterials.minus(setOf(toDeleteMaterial))),
                showSaveChangesButton = true
            )
        }
    }

    private fun saveChanges() {
        _uiState.update {
            it.copy(
                showSaveChangesButton = false
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            if (courseId == null) {
                serverRepository.createCourse(uiState.value.course)
            } else {
                serverRepository.updateCourse(courseId, uiState.value.course)
            }
        }
    }
}