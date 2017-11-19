package ru.spbau.mit.latex

abstract class TextMode: Block() {

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

class TextTag(override val attributes: AttributeList): TextMode() {
    override val name: String get() = "text"
}
