package app.data.fileManager

data class FilePickerRequest(
    val action: FileManager.Action,
    val extensions: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilePickerRequest

        if (action != other.action) return false
        if (!extensions.contentEquals(other.extensions)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = action.hashCode()
        result = 31 * result + extensions.contentHashCode()
        return result
    }

}