package ru.spbau.mit.latex

import java.io.OutputStream

abstract class Element {

    // public:
    abstract fun render(os: OutputStream, indent: String)

    fun print(os: OutputStream = System.out) = render(os, "")

    // protected:
    protected fun renderLine(line: String, os: OutputStream, indent: String) {
        os.write(line.trimMargin().prependIndent(indent).trimEnd().toByteArray())
        os.write("\n".toByteArray())
    }
}

class TextElement(val text: String): Element() {
    override fun render(os: OutputStream, indent: String) = renderLine(text, os, indent)
}
