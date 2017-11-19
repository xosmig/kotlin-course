package ru.spbau.mit.latex

import java.io.OutputStream

fun beamerFile(vararg attributes: Attribute, init: BeamerFile.() -> Unit): BeamerFile =
        BeamerFile(attributes).apply(init)

class BeamerFile(attributes: AttributeList): TexFile("beamer", attributes) {
    fun document(init: BeamerDocument.() -> Unit): BeamerDocument = initTag(BeamerDocument(), init)
}

class BeamerDocument: Document() {

    // public:
    fun frame(vararg attributes: Attribute, init: Frame.() -> Unit): Frame =
            initTag(Frame(attributes), init)

    // nested classes:
    class Frame(attributes: AttributeList): TextMode("frame", attributes) {

        fun title(title: String): Title = addChild(Title(title))

        class Title(val title: String): Element() {
            override fun render(os: OutputStream, indent: String) =
                    renderLine("\\frametitle{$title}", os, indent)
        }
    }
}
