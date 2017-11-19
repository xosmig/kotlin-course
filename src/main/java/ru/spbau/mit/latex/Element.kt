package ru.spbau.mit.latex

import java.io.OutputStream

abstract class Element {

    // public:
    abstract fun render(os: OutputStream, indent: String)

    fun print(os: OutputStream = System.out) = render(os, "")

    // protected:
    protected fun renderLine(s: String, os: OutputStream, indent: String) {
        os.write(s.trimMargin().prependIndent(indent).toByteArray())
        os.write("\n".toByteArray())
    }
}

class TextElement(val text: String): Element() {

    // public:
    override fun render(os: OutputStream, indent: String) = renderLine(text, os, indent)
}

class EmptyLine: Element() {

    // public:
    override fun render(os: OutputStream, indent: String) =
            renderLine("", os, indent)
}

