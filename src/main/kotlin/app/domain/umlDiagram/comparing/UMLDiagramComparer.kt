package app.domain.umlDiagram.comparing

class UMLDiagramComparer {

    fun compare(standard: CompareData, target: CompareData): Mark {
        return Mark(
            componentPresence = evaluateComponentPresence(standard, target),
            connectionPresence = evaluateConnectionPresence(standard, target),
            componentAccordance = evaluateComponentAccordance(standard, target),
            connectionAccordance = evaluateConnectionAccordance(standard, target)
        )
    }

    private fun evaluateComponentPresence(standard: CompareData, target: CompareData): Float {
        //TODO

        return 0f
    }

    private fun evaluateConnectionPresence(standard: CompareData, target: CompareData): Float {
        //TODO

        return 0f
    }

    private fun evaluateComponentAccordance(standard: CompareData, target: CompareData): Float {
        //TODO

        return 0f
    }

    private fun evaluateConnectionAccordance(standard: CompareData, target: CompareData): Float {
        //TODO

        return 0f
    }
}