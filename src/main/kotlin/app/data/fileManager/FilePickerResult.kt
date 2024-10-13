package app.data.fileManager

import java.io.*

data class FilePickerResult(
    val action: FileManager.Action,
    val file: File
)