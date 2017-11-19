package ru.spbau.mit.latex

import java.io.OutputStream

class BeamerFile: File<BeamerDocument>() {

    // public
    override val documentClass: String get() = "beamer"

    override fun document(init: BeamerDocument.() -> Unit): BeamerDocument = initTag(BeamerDocument(), init)
}

fun beamerFile(init: BeamerFile.() -> Unit): BeamerFile = BeamerFile().apply(init)

class BeamerDocument: Document() {
    // public:
    fun frame(vararg attributes: Attribute, init: Frame.() -> Unit): Frame =
            initTag(Frame(attributes), init)

    // nested classes:
    class Frame(override val attributes: AttributeList): TextMode() {
        override val name: String get() = "frame"

        fun title(title: String): Title = addChild(Title(title))

        class Title(val title: String): Element() {
            override fun render(os: OutputStream, indent: String) =
                    renderLine("\\frametitle{$title}", os, indent)
        }
    }
}
