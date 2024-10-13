package app.domain.umlDiagram.model.connection

import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.UMLClassConnection.*
import kotlinx.serialization.*

@Serializable
sealed class RefConnection(open var ref: UMLClassComponent) {

    @Serializable
    data class SimpleConnection(
        @SerialName("simpleRef") override var ref: UMLClassComponent,
        val offset: ConnectionOffset = ConnectionOffset()
    ) : RefConnection(ref)

    @Serializable
    data class ReferencedConnection(
        @SerialName("referencedRef") override var ref: UMLClassComponent,
        val refType: RefType
    ) : RefConnection(ref)
}

@Serializable
sealed class RefType(open val index: Int) {
    @Serializable
    data class Field(@SerialName("fieldId") override val index: Int) : RefType(index)
    @Serializable
    data class Function(@SerialName("functionId") override val index: Int) : RefType(index)
}

fun RefConnection.getOffsets(): ConnectionOffset? {
    return when (this) {
        is RefConnection.ReferencedConnection -> null
        is RefConnection.SimpleConnection -> offset
    }
}