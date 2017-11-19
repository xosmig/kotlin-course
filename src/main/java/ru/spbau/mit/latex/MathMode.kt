package ru.spbau.mit.latex

abstract class MathMode: Block() {

    // public:
    fun text(init: TextTag.() -> Unit): TextMode = initTag(TextTag(emptyArray()), init)
}

class MathTag(override val attributes: AttributeList): MathMode() {
    override val name: String get() = "math"
}
