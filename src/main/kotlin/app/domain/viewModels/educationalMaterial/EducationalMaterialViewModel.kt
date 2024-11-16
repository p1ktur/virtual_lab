package app.domain.viewModels.educationalMaterial

import app.data.server.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import moe.tlaster.precompose.viewmodel.*

class EducationalMaterialViewModel(
    private val courseId: Int,
    private val educationalMaterialId: Int?,
    private val serverRepository: ServerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EducationalMaterialUiState(serverRepository.authenticatedType.value))
    val uiState = _uiState.asStateFlow()

    fun onUiAction(action: EducationalMaterialUiAction) {
        viewModelScope.launch {
            when (action) {
                EducationalMaterialUiAction.FetchData -> fetchData()

                is EducationalMaterialUiAction.UpdateName -> updateName(action.name)
                is EducationalMaterialUiAction.UpdateDescription -> updateDescription(action.description)

                EducationalMaterialUiAction.AddURL -> addURL()
                is EducationalMaterialUiAction.UpdateURL -> updateURL(action.index, action.url)
                is EducationalMaterialUiAction.DeleteURL -> deleteURL(action.index)

                EducationalMaterialUiAction.SaveChanges -> saveChanges()
                EducationalMaterialUiAction.DeleteEducationalMaterial -> deleteEducationalMaterial()
            }
        }
    }

    private fun fetchData() {
        educationalMaterialId?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                val educationalMaterial = serverRepository.getEducationalMaterial(id)

                if (educationalMaterial != null) {
                    _uiState.update {
                        it.copy(
                            educationalMaterial = educationalMaterial
                        )
                    }
                }
            }
        } ?: _uiState.update {
            it.copy(
                educationalMaterial = it.educationalMaterial.copy(courseId = courseId)
            )
        }
    }

    private fun updateName(name: String) {
        _uiState.update {
            it.copy(
                educationalMaterial = it.educationalMaterial.copy(name = name),
                showSaveChangesButton = true
            )
        }
    }

    private fun updateDescription(description: String) {
        _uiState.update {
            it.copy(
                educationalMaterial = it.educationalMaterial.copy(description = description),
                showSaveChangesButton = true
            )
        }
    }

    private fun addURL() {
        _uiState.update {
            it.copy(
                educationalMaterial = it.educationalMaterial.copy(
                    urls = it.educationalMaterial.urls + ""
                ),
                showSaveChangesButton = true
            )
        }
    }

    private fun updateURL(index: Int, url: String) {
        val newUrls = uiState.value.educationalMaterial.urls.toMutableList().apply {
            this[index] = url
        }

        _uiState.update {
            it.copy(
                educationalMaterial = it.educationalMaterial.copy(
                    urls = newUrls
                ),
                showSaveChangesButton = true
            )
        }
    }

    private fun deleteURL(index: Int) {
        val toDeleteURL = uiState.value.educationalMaterial.urls[index]

        _uiState.update {
            it.copy(
                educationalMaterial = it.educationalMaterial.copy(urls = it.educationalMaterial.urls.minus(setOf(toDeleteURL))),
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
            if (educationalMaterialId == null) {
                serverRepository.addEducationalMaterialToCourse(uiState.value.educationalMaterial)?.let { newId ->
                    _uiState.update {
                        it.copy(
                            educationalMaterial = it.educationalMaterial.copy(id = newId)
                        )
                    }
                }
            } else {
                serverRepository.updateEducationalMaterial(educationalMaterialId, uiState.value.educationalMaterial)
            }
        }
    }

    private fun deleteEducationalMaterial() {
        if (educationalMaterialId == null) return

        viewModelScope.launch(Dispatchers.IO) {
            serverRepository.deleteEducationalMaterial(educationalMaterialId)
        }
    }
}