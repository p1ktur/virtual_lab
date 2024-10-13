package app.domain.util.fileManager

import androidx.compose.runtime.*
import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import kotlinx.coroutines.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.io.*

class FileManager {

    enum class Action {
        SAVE,
        SAVE_TO,
        LOAD
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        prettyPrint = true
        explicitNulls = true
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
        } catch (_: Exception) {}
    }
}