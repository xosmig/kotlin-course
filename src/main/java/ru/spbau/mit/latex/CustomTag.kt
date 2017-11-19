package ru.spbau.mit.latex

class CustomTextTag(override val name: String, override val attributes: Array<out Attribute>): TextMode()

class CustomMathTag(override val name: String, override val attributes: Array<out Attribute>): MathMode()
