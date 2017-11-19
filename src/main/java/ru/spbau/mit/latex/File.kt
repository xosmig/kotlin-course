package ru.spbau.mit.latex

import java.io.OutputStream

abstract class File<out D: Document>(documentClass: String, attributes: AttributeList): Tag(documentClass, attributes) {

    val documentClass get() = name

    // public:
    override fun render(os: OutputStream, indent: String) {
        renderLine("\\documentclass{$documentClass}${attributesString()}", os, indent)
        renderLine("", os, indent)
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

class CustomFile(documentClass: String, attributes: AttributeList): File<CustomDocument>(documentClass, attributes) {
    override fun document(init: CustomDocument.() -> Unit): CustomDocument =
            initTag(CustomDocument(), init)
}

fun customFile(documentClass: String, vararg attributes: Attribute, init: CustomFile.() -> Unit): CustomFile =
        CustomFile(documentClass, attributes).apply(init)
