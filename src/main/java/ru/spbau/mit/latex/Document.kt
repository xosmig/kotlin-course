package ru.spbau.mit.latex

abstract class Document: TextMode("document", emptyArray())

class CustomDocument: Document()

class ArticleDocument: Document()
