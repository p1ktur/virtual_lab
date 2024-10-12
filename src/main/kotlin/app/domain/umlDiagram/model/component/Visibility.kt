package app.domain.umlDiagram.model.component

enum class Visibility(val symbol: Char) {
    PUBLIC('+'),
    PRIVATE('-'),
    PROTECTED('#'),
    PACKAGE('~');
}