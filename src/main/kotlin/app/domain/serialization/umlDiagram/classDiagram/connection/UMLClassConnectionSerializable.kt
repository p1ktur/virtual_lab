package app.domain.serialization.umlDiagram.classDiagram.connection

import app.domain.umlDiagram.classDiagram.component.*
import app.domain.umlDiagram.classDiagram.connection.*
import app.presenter.canvas.arrows.*
import kotlinx.serialization.*

@Serializable
data class UMLClassConnectionSerializable(
    // Data
    var name: String = "",
    var startText: String = "",
    var endText: String = "",
    var startRef: RefConnectionSerializable,
    var endRef: RefConnectionSerializable,
    var startArrowHead: ArrowHead = ArrowHead.ASSOCIATION,
    var endArrowHead: ArrowHead = ArrowHead.ASSOCIATION,
    var arrowType: ArrowType = ArrowType.SOLID,
    // Graphics
    var middleOffset: Float = 0.5f,
    var middleArchOffset: Float = 0.5f,
    var forcedType: UMLClassConnection.Type? = null
) {
    fun toOriginal(components: List<UMLClassComponent>): UMLClassConnection {
        return UMLClassConnection(
            name = name,
            startText = startText,
            endText = endText,
            startRef = when (startRef) {
                is RefConnectionSerializable.Simple -> RefConnection.SimpleConnection(
                    ref = components[startRef.getRefIndex()],
                    offset = (startRef as RefConnectionSerializable.Simple).offset
                )
                is RefConnectionSerializable.Referenced -> RefConnection.ReferencedConnection(
                    ref = components[startRef.getRefIndex()],
                    refType = (startRef as RefConnectionSerializable.Referenced).refType
                )
            },
            endRef = when (endRef) {
                is RefConnectionSerializable.Simple -> RefConnection.SimpleConnection(
                    ref = components[endRef.getRefIndex()],
                    offset = (endRef as RefConnectionSerializable.Simple).offset
                )
                is RefConnectionSerializable.Referenced -> RefConnection.ReferencedConnection(
                    ref = components[endRef.getRefIndex()],
                    refType = (endRef as RefConnectionSerializable.Referenced).refType
                )
            },
            startArrowHead = startArrowHead,
            endArrowHead = endArrowHead,
            arrowType = arrowType,
            middleOffset = middleOffset,
            middleArchOffset = middleArchOffset,
            forcedType = forcedType
        )
    }
}