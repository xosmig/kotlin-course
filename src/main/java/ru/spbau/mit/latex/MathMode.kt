package ru.spbau.mit.latex

abstract class MathMode(name: String, attributes: AttributeList): Tag(name, attributes) {

    // public:
    fun text(init: TextTag.() -> Unit): TextMode = initTag(TextTag(emptyArray()), init)
}

class MathTag(attributes: AttributeList): MathMode("math", attributes)
