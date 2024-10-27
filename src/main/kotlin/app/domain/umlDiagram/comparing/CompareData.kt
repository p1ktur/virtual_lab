package app.domain.umlDiagram.comparing

import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.*

data class CompareData(
    val components: List<UMLClassComponent>,
    val connections: List<UMLClassConnection>
)
