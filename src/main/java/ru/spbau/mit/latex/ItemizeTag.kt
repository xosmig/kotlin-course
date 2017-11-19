package ru.spbau.mit.latex

import java.io.OutputStream

class ItemizeTag(override val attributes: AttributeList): TextMode() {
    override val name: String get() = "itemize"

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
