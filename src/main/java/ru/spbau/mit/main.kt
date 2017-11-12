package ru.spbau.mit

import java.io.FileInputStream

fun interpretFile(path: String) {
    FileInputStream(path).use {
        Parser.parse(it).perform()
    }
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: hwlang source.hw")
        System.exit(2)
    }
    interpretFile(args[0])
}
