package ru.spbau.mit

import ru.spbau.mit.latex.beamerFile

fun main(args: Array<String>) {
    beamerFile("12pt") {
        usepackage("babel", "russian" /* varargs */)

        document {
            frame {
                title("frametitle")

                itemize {
                    for (row in 1..4) {
                        item { + "$row text" }
                    }
                }

                customTextTag("pyglist", "language=kotlin") {
                    +"""
                    |val a = 1
                    |
                    """
                }
            }
        }
    }.print()

}
