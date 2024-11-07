package app.data.fileManager

import app.domain.serialization.umlDiagram.classDiagram.connection.*
import app.domain.umlDiagram.comparing.*
import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.*
import kotlinx.serialization.Serializable

@Serializable
data class SaveDataDeprecated(
    val components: List<UMLClassComponent>,
    val connections: List<UMLClassConnection>
)

@Serializable
data class SaveData(
    val components: List<UMLClassComponent>,
    val connections: List<UMLClassConnectionSerializable>
) {
    fun toCompareData(): CompareData {
        return CompareData(components, connections.map { it.toOriginal(components) })
    }
}