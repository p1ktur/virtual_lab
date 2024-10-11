package app.domain.util.geometry

import androidx.compose.ui.geometry.*
import app.domain.umlDiagram.model.components.*
import app.domain.umlDiagram.mouse.*

fun UMLClassComponent.checkSidesForContainment(position: Offset, mousePosition: Offset) : ContainmentResult {
    val containsLeft = mousePosition.x in (position.x - ContainmentResult.SIDE_CHECK_RADIUS)..(position.x + ContainmentResult.SIDE_CHECK_RADIUS) &&
            mousePosition.y in (position.y + ContainmentResult.SIDE_CHECK_RADIUS)..(position.y + size.height - ContainmentResult.SIDE_CHECK_RADIUS)

    if (containsLeft) return ContainmentResult.Side(SideDirection.LEFT)

    val containsTop = mousePosition.x in (position.x + ContainmentResult.SIDE_CHECK_RADIUS)..(position.x + size.width - ContainmentResult.SIDE_CHECK_RADIUS) &&
            mousePosition.y in (position.y - ContainmentResult.SIDE_CHECK_RADIUS)..(position.y + ContainmentResult.SIDE_CHECK_RADIUS)

    if (containsTop) return ContainmentResult.Side(SideDirection.TOP)

    val containsRight = mousePosition.x in (position.x + size.width - ContainmentResult.SIDE_CHECK_RADIUS)..(position.x + size.width + ContainmentResult.SIDE_CHECK_RADIUS) &&
            mousePosition.y in (position.y + ContainmentResult.SIDE_CHECK_RADIUS)..(position.y + size.height - ContainmentResult.SIDE_CHECK_RADIUS)

    if (containsRight) return ContainmentResult.Side(SideDirection.RIGHT)

    val containsBottom = mousePosition.x in (position.x + ContainmentResult.SIDE_CHECK_RADIUS)..(position.x + size.width - ContainmentResult.SIDE_CHECK_RADIUS) &&
            mousePosition.y in (position.y + size.height - ContainmentResult.SIDE_CHECK_RADIUS)..(position.y + size.height + ContainmentResult.SIDE_CHECK_RADIUS)

    if (containsBottom) return ContainmentResult.Side(SideDirection.BOTTOM)

    return ContainmentResult.None
}

fun UMLClassComponent.checkVerticesForContainment(position: Offset, mousePosition: Offset) : ContainmentResult {
    val topLeft = position
    val containsTopLeft = mousePosition.isAround(topLeft, ContainmentResult.VERTEX_CHECK_RADIUS)
    if (containsTopLeft) return ContainmentResult.Vertex(VertexDirection.TOP_LEFT)

    val topRight = Offset(position.x + size.width, position.y)
    val containsTopRight = mousePosition.isAround(topRight, ContainmentResult.VERTEX_CHECK_RADIUS)
    if (containsTopRight) return ContainmentResult.Vertex(VertexDirection.TOP_RIGHT)

    val bottomLeft = Offset(position.x, position.y + size.height)
    val containsBottomLeft = mousePosition.isAround(bottomLeft, ContainmentResult.VERTEX_CHECK_RADIUS)
    if (containsBottomLeft) return ContainmentResult.Vertex(VertexDirection.BOTTOM_LEFT)

    val bottomRight = Offset(position.x + size.width, position.y + size.height)
    val containsBottomRight = mousePosition.isAround(bottomRight, ContainmentResult.VERTEX_CHECK_RADIUS)
    if (containsBottomRight) return ContainmentResult.Vertex(VertexDirection.BOTTOM_RIGHT)

    return ContainmentResult.None
}