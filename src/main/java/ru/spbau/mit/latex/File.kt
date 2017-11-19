package ru.spbau.mit.latex

import java.io.OutputStream

abstract class File<D: Document>: Tag() {
    abstract val documentClass: String

    // public:
    override fun render(os: OutputStream, indent: String) {
        renderLine("\\documentclass{$documentClass}", os, indent)
        renderChildren(os, indent)
    }

    fun usepackage(packageName: String, vararg params: String) = addChild(Usepackage(packageName))

    abstract fun document(init: D.() -> Unit): D

    class Usepackage(val packageName: String): Element() {
        override fun render(os: OutputStream, indent: String) {
            renderLine("\\usepackage{$packageName}", os, indent)
        }
    }
}

class CustomFile(override val documentClass: String): File<CustomDocument>() {
    override fun document(init: CustomDocument.() -> Unit): CustomDocument =
            initTag(CustomDocument(), init)
}

fun customFile(documentClass: String, init: CustomFile.() -> Unit): CustomFile =
        CustomFile(documentClass).apply(init)
