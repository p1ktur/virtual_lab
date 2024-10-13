package app.domain.util.fileManager

import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*
import kotlinx.serialization.Serializable

@Serializable
data class SaveData(
    val components: List<UMLClassComponent>,
    val connections: List<UMLClassConnection>
)