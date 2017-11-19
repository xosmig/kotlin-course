package ru.spbau.mit.latex

import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.nio.charset.Charset

@DslMarker
annotation class TexTagMarker

typealias Attribute = Pair<String, String>
typealias AttributeList = Array<out Attribute>

@TexTagMarker
abstract class Tag: Element() {

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

    // protected:
    protected fun <T: Element> addChild(child: T): T = child.apply { childrenMutable.add(this) }

    protected fun renderChildren(os: OutputStream, indent: String) = children.forEach { it.render(os, indent) }

    protected fun <T : Tag> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        childrenMutable.add(tag)
        return tag
    }
}

fun adjustIndentation(indent: String): String = "$indent\t"
