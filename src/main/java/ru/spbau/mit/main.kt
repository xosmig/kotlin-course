package ru.spbau.mit

import java.io.FileInputStream

fun interpretFile(path: String) {
    FileInputStream(path).use {
        try {
            Parser.parse(it).perform()
        } catch (e: Exception) {
            println(e)
            System.exit(3)
        }
    }
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Usage: hwlang source.hw")
        System.exit(2)
    }
    interpretFile(args[0])
}
