package ru.spbau.mit.latex

import java.io.OutputStream

abstract class Block: Tag() {

    abstract val name: String
    abstract val attributes: AttributeList

    // public:
    override fun render(os: OutputStream, indent: String) {
        renderBegin(os, indent)
        renderChildren(os, adjustIndentation(indent))
        renderEnd(os, indent)
    }

    // protected:
    protected fun renderBegin(os: OutputStream, indent: String) {
        val attributesString = if (attributes.isEmpty()) {
            ""
        } else {
            val joinedAttributes = attributes.joinToString { "${it.first}=${it.second}" }
            "[$joinedAttributes]"
        }
        renderLine("\\begin{$name}$attributesString", os, indent)
    }

    protected fun renderEnd(os: OutputStream, indent: String) = renderLine("\\end{$name}", os, indent)
}
