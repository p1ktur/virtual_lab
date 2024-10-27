package app.domain.umlDiagram.classDiagram.component

enum class Visibility(val symbol: Char) {
    PUBLIC('+'),
    PRIVATE('-'),
    PROTECTED('#'),
    PACKAGE('~');
}