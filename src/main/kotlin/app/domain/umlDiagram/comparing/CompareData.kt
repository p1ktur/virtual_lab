package app.domain.umlDiagram.comparing

import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*

data class CompareData(
    val components: List<UMLClassComponent>,
    val connections: List<UMLClassConnection>
)
