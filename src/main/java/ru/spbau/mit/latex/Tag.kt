package ru.spbau.mit.latex

import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.charset.Charset

@DslMarker
annotation class TexTagMarker

typealias Attribute = String
typealias AttributeList = Array<out Attribute>

@TexTagMarker
abstract class Tag(val name: String, val attributes: AttributeList): Element() {

    private val childrenMutable = ArrayList<Element>()
    val children: List<Element> get() = childrenMutable

    // public:
    fun customTextBlock(name: String, vararg attributes: Attribute, init: CustomTextTag.() -> Unit): CustomTextTag =
            addChild(CustomTextTag(name, attributes).apply(init))

    fun customMathBlock(name: String, vararg attributes: Attribute, init: CustomMathTag.() -> Unit): CustomMathTag =
            addChild(CustomMathTag(name, attributes).apply(init))

    operator fun String.unaryPlus(): TextElement = addChild(TextElement(this))

    override fun toString(): String {
        ByteArrayOutputStream().use {
            render(it, "")
            return it.toByteArray().toString(Charset.defaultCharset())
        }
    }

    override fun render(os: OutputStream, indent: String) {
        renderBegin(os, indent)
        renderChildren(os, adjustIndentation(indent))
        renderEnd(os, indent)
    }

    // protected:
    protected fun <T: Element> addChild(child: T): T = child.apply { childrenMutable.add(this) }

    protected fun renderBegin(os: OutputStream, indent: String) =
        renderLine("\\begin{$name}${attributesString()}", os, indent)

    protected fun attributesString(): String =
            if (attributes.isEmpty()) { "" } else { "[${attributes.joinToString()}]" }

    protected fun renderChildren(os: OutputStream, indent: String) = children.forEach { it.render(os, indent) }

    protected fun renderEnd(os: OutputStream, indent: String) = renderLine("\\end{$name}", os, indent)

    protected fun <T : Tag> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        childrenMutable.add(tag)
        return tag
    }
}

fun adjustIndentation(indent: String): String = "$indent\t"
