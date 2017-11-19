package ru.spbau.mit.latex

abstract class Document: TextMode() {
    override val name: String get() = "document"
    override val attributes: AttributeList get() = emptyArray()
}

class CustomDocument: Document()
