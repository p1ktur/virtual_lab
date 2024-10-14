package app.domain.umlDiagram.comparing

import app.domain.umlDiagram.model.component.*
import app.domain.umlDiagram.model.connection.*

class UMLDiagramComparer {

    fun compare(standard: CompareData, target: CompareData): Mark {
        val (componentPresenceMark, presentComponents) = evaluateComponentPresence(standard, target)
        val (connectionPresenceMark, presentConnections) = evaluateConnectionPresence(standard, target)

        return Mark(
            componentPresence = componentPresenceMark,
            connectionPresence = connectionPresenceMark,
            componentAccordance = evaluateComponentAccordance(target, presentComponents),
            connectionAccordance = evaluateConnectionAccordance(target, presentConnections)
        )
    }

    private fun evaluateComponentPresence(standard: CompareData, target: CompareData): Pair<Float, List<UMLClassComponent>> {
        val sNames = standard.components.map { it.name }.toMutableList()
        val tNames = target.components.map { it.name }.toMutableList()
        val allNames = sNames + tNames.filter { !sNames.contains(it) }

        var presence = 0
        val presentComponents = mutableListOf<UMLClassComponent>()

        for (name in allNames) {
            if (tNames.contains(name) && sNames.contains(name)) {
                presentComponents.add(standard.components[presence + sNames.indexOf(name)])

                sNames.remove(name)
                tNames.remove(name)

                presence++
            }
        }

        val mark = presence.toFloat() / allNames.size

        return mark to presentComponents
    }

    private fun evaluateConnectionPresence(standard: CompareData, target: CompareData): Pair<Float, List<UMLClassConnection>> {
        val sNames = standard.connections.map { it.getLongerName() }.toMutableList()
        val tNames = target.connections.map { it.getLongerName() }.toMutableList()
        val allNames = sNames + tNames.filter { !sNames.contains(it) }

        var presence = 0
        val presentComponents = mutableListOf<UMLClassConnection>()

        for (name in allNames) {
            if (tNames.contains(name) && sNames.contains(name)) {
                presentComponents.add(standard.connections[presence + sNames.indexOf(name)])

                sNames.remove(name)
                tNames.remove(name)

                presence++
            }
        }

        val mark = presence.toFloat() / allNames.size

        return mark to presentComponents
    }

    private fun evaluateComponentAccordance(target: CompareData, presentComponents: List<UMLClassComponent>): Float {
        if (presentComponents.isEmpty()) return 0f

        val presentComponentsCopy = presentComponents.toMutableList()
        val leftTargetComponents = target.components.toMutableList()
        val tComponents = mutableListOf<UMLClassComponent>()

        while (presentComponentsCopy.isNotEmpty()) {
            leftTargetComponents.firstOrNull { it.name == presentComponentsCopy[0].name }?.let {
                tComponents.add(it)
            }
            presentComponentsCopy.removeAt(0)
            leftTargetComponents.removeAt(0)
        }

        var accordance = 0f

        for (index in presentComponents.indices) {
            if (index >= tComponents.size) break
            var currentAccordance = 0f
            var total = 2

            val s = presentComponents[index]
            val t = tComponents[index]

            val sFieldNames = s.fields.map { it.toString() }
            val tFieldNames = t.fields.map { it.toString() }

            val sFunctionNames = s.functions.map { it.toString() }
            val tFunctionNames = t.functions.map { it.toString() }

            if (s.name == t.name) currentAccordance++
            if (s.isInterface == t.isInterface) currentAccordance++

            if (sFieldNames.isNotEmpty()) {
                currentAccordance += tFieldNames.count { sFieldNames.contains(it) }.toFloat() / sFieldNames.size
                total++
            } else if (tFieldNames.isNotEmpty()) {
                total++
            }
            if (sFunctionNames.isNotEmpty()) {
                currentAccordance += tFunctionNames.count { sFunctionNames.contains(it) }.toFloat() / sFunctionNames.size
                total++
            }else if (tFunctionNames.isNotEmpty()) {
                total++
            }

            accordance += currentAccordance / total
        }

        val mark = accordance / presentComponents.size

        return mark
    }

    private fun evaluateConnectionAccordance(target: CompareData, presentConnections: List<UMLClassConnection>): Float {
        if (presentConnections.isEmpty()) return 0f

        val presentConnectionsCopy = presentConnections.toMutableList()
        val leftTargetConnections = target.connections.toMutableList()
        val tConnections = mutableListOf<UMLClassConnection>()

        while (presentConnectionsCopy.isNotEmpty()) {
            leftTargetConnections.firstOrNull { it.getLongerName() == presentConnectionsCopy[0].getLongerName() }?.let {
                tConnections.add(it)
            }
            presentConnectionsCopy.removeAt(0)
            leftTargetConnections.removeAt(0)
        }

        var accordance = 0f

        for (index in presentConnections.indices) {
            if (index >= tConnections.size) break
            var currentAccordance = 0f
            val total = 6

            val s = presentConnections[index]
            val t = tConnections[index]

            if (s.name == t.name) currentAccordance++
            if (s.startText == t.startText) currentAccordance++
            if (s.endText == t.endText) currentAccordance++
            if (s.startArrowHead == t.startArrowHead) currentAccordance++
            if (s.endArrowHead == t.endArrowHead) currentAccordance++
            if (s.arrowType == t.arrowType) currentAccordance++

            accordance += currentAccordance / total
        }

        val mark = accordance / presentConnections.size

        return mark
    }
}