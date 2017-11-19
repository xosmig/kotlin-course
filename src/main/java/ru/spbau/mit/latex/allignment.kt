package ru.spbau.mit.latex

class FlushleftTag(override val attributes: AttributeList): TextMode() {
    override val name: String get() = "flushleft"
}

class FlushrightTag(override val attributes: AttributeList): TextMode() {
    override val name: String get() = "flushright"
}

class CenterTag(override val attributes: AttributeList): TextMode() {
    override val name: String get() = "center"
}
