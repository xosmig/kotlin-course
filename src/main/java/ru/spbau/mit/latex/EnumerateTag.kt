package ru.spbau.mit.latex

import java.io.OutputStream

class EnumerateTag(override val attributes: AttributeList): TextMode() {
    override val name: String get() = "enumerate"

    // public:
    fun item(vararg attributes: Attribute, init: Item.() -> Unit): Item = initTag(Item(attributes), init)

    // nested classes:
    class Item(override val attributes: AttributeList): TextMode() {
        override val name: String get() = "item"

        override fun render(os: OutputStream, indent: String) {
            renderLine("\\item", os, indent)
            renderChildren(os, adjustIndentation(indent))
        }
    }
}
