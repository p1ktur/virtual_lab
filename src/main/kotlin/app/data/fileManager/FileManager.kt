package app.data.fileManager

import androidx.compose.runtime.*
import app.data.server.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import java.io.*

class FileManager {

    enum class Action {
        SAVE,
        SAVE_TO,
        LOAD
    }

    private val json = ServerJson.get()

    companion object {
        val saveExtensions = arrayOf("txt", "json")
    }

    var openedSaveFile: MutableState<File?> = mutableStateOf(null)

    var onRequestSaveData: (() -> SaveData)? = null
    var onDeliverSaveData: ((SaveData) -> Unit)? = null

    suspend fun save() = withContext(Dispatchers.IO) {
        val saveData = onRequestSaveData?.invoke() ?: return@withContext

        openedSaveFile.value?.let { file ->
            val saveDataJson = json.encodeToString(saveData)
            file.writeText(saveDataJson)
        }
    }

    suspend fun saveTo(file: File) = withContext(Dispatchers.IO) {
        val saveData = onRequestSaveData?.invoke() ?: return@withContext
        val saveDataJson = json.encodeToString(saveData)

        if (file.extension.isBlank()) {
            var fileInFolder = File(file.path + "/class_diagram.json")
            var index = 1

            while (fileInFolder.exists()) {
                fileInFolder = File(file.path + "/class_diagram_$index.json")
                index++
            }

            openedSaveFile.value = fileInFolder
            fileInFolder.writeText(saveDataJson)
        } else {
            openedSaveFile.value = file
            file.writeText(saveDataJson)
        }
    }

    suspend fun load(file: File) = withContext(Dispatchers.IO) {
        val saveDataJson = file.readText()

        openedSaveFile.value = file

        try {
            val saveData = json.decodeFromString<SaveData>(saveDataJson)
            onDeliverSaveData?.invoke(saveData)
        } catch (_: Exception) {
            try {
                val saveData = json.decodeFromString<SaveDataDeprecated>(saveDataJson)
                onDeliverSaveData?.invoke(
                    SaveData(
                        components = saveData.components,
                        connections = saveData.connections.onEach {
                            it.findAndApplyCorrectReferences(saveData.components)
                        }.map {
                            it.toSerializable(saveData.components)
                        }
                    )
                )
            } catch (_: Exception) {
                openedSaveFile.value = null
                onDeliverSaveData?.invoke(
                    SaveData(
                        components = emptyList(),
                        connections = emptyList()
                    )
                )
            }
        }
    }

    private fun UMLClassConnection.findAndApplyCorrectReferences(references: List<UMLClassComponent>) {
        for (index in references.indices) {
            if (references[index].equalsTo(startRef.getRefClass())) {
                startRef.setRefClass(references[index])
                break
            }
        }

        for (index in references.indices) {
            if (references[index].equalsTo(endRef.getRefClass()) && !references[index].equalsTo(startRef.getRefClass())) {
                endRef.setRefClass(references[index])
                break
            }
        }
    }
}