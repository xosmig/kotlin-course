package ru.spbau.mit

import ru.spbau.mit.latex.beamerFile

fun main(args: Array<String>) {
    beamerFile {
        usepackage("babel", "russian" /* varargs */)

        document {
            frame("arg1" to "arg2") {
                title("frametitle")

                itemize {
                    for (row in 1..4) {
                        item { + "$row text" }
                    }
                }

                customTextBlock("pyglist", "language" to "kotlin") {
                    +"""
                    |val a = 1
                    |
                    """
                }
            }
        }
    }.print()

}
