package ru.spbau.mit

import java.io.OutputStream

fun interpretFile(path: String, stdout: OutputStream = System.out): Int {
    try {
        Parser.parseFile(path).perform(stdout)
    } catch (e: Exception) {
        println(e)
        return 3
    }
    return 0
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: hwlang source.hw")
        System.exit(2)
    }
    System.exit(interpretFile(args[0]))
}
