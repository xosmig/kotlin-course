package ru.spbau.mit.latex

import java.io.OutputStream

class ItemizeTag(attributes: AttributeList): TextMode("itemize", attributes) {

    // public:
    fun item(vararg attributes: Attribute, init: Item.() -> Unit): Item = initTag(Item(attributes), init)

    // nested classes:
    class Item(attributes: AttributeList): TextMode("item", attributes) {
        override fun render(os: OutputStream, indent: String) {
            renderLine("\\item", os, indent)
            renderChildren(os, adjustIndentation(indent))
        }
    }
}
