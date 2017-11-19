package ru.spbau.mit.latex

import java.io.OutputStream

abstract class TextMode(name: String, attributes: AttributeList): Tag(name, attributes) {

    // public:
    fun math(init: MathTag.() -> Unit): MathMode = initTag(MathTag(emptyArray()), init)

    fun flushleft(init: FlushleftTag.() -> Unit): FlushleftTag = initTag(FlushleftTag(emptyArray()), init)

    fun flushright(init: FlushrightTag.() -> Unit): FlushrightTag = initTag(FlushrightTag(emptyArray()), init)

    fun center(init: CenterTag.() -> Unit): CenterTag = initTag(CenterTag(emptyArray()), init)

    fun itemize(vararg attributes: Attribute, init: ItemizeTag.() -> Unit): ItemizeTag =
            initTag(ItemizeTag(attributes), init)

    fun enumerate(vararg attributes: Attribute, init: EnumerateTag.() -> Unit): EnumerateTag =
            initTag(EnumerateTag(attributes), init)

    fun emptyLine() = addChild(EmptyLine())
}

class TextTag(attributes: AttributeList): TextMode("text", attributes)

class EmptyLine: Element() {
    override fun render(os: OutputStream, indent: String) = renderLine("", os, indent)
}
