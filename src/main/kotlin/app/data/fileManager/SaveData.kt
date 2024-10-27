package app.data.fileManager

import app.domain.umlDiagram.comparing.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.*
import kotlinx.serialization.Serializable

@Serializable
data class SaveData(
    val components: List<UMLClassComponent>,
    val connections: List<UMLClassConnection>
) {
    fun toCompareData(): CompareData {
        return CompareData(components, connections)
    }
}