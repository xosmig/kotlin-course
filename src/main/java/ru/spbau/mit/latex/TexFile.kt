package ru.spbau.mit.latex

import java.io.OutputStream

abstract class TexFile(documentClass: String, attributes: AttributeList): Tag(documentClass, attributes) {

    val documentClass get() = name

    // public:
    override fun render(os: OutputStream, indent: String) {
        renderLine("\\documentclass{$documentClass}${attributesString()}", os, indent)
        renderLine("", os, indent)
        renderChildren(os, indent)
    }

    fun usepackage(packageName: String, vararg params: String) = addChild(Usepackage(packageName))

    class Usepackage(val packageName: String): Element() {
        override fun render(os: OutputStream, indent: String) {
            renderLine("\\usepackage{$packageName}", os, indent)
        }
    }
}

class CustomFile(documentClass: String, attributes: AttributeList): TexFile(documentClass, attributes) {
    fun document(init: CustomDocument.() -> Unit): CustomDocument = initTag(CustomDocument(), init)
}

class Article(attributes: AttributeList): TexFile("article", attributes) {
    fun document(init: ArticleDocument.() -> Unit): ArticleDocument = initTag(ArticleDocument(), init)
}

fun customFile(documentClass: String, vararg attributes: Attribute, init: CustomFile.() -> Unit): CustomFile =
        CustomFile(documentClass, attributes).apply(init)

fun article(vararg attributes: Attribute, init: Article.() -> Unit): Article = Article(attributes).apply(init)
