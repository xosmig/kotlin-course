package ru.spbau.mit

import org.junit.Test
import java.io.ByteArrayInputStream

class IntegrationTest: InterpreterTestBase() {
    private fun checkOutput(code: String, expectedOutput: String) {
        checkOutput(Parser.parse(ByteArrayInputStream(code.toByteArray())), expectedOutput)
    }

    @Test
    fun helloWorldTest() {
        val code = """
            |println(43110, 111)
        """.trimMargin()
        checkOutput(code, """
            |43110, 111
            |
        """.trimMargin())
    }

    @Test
    fun fibTest() {
        val code = """
            |fun fib(n) {
            |   if (n <= 1) {
            |       return 1
            |   }
            |   return fib(n - 1) + fib(n - 2)
            |}
            |
            |var i = 1
            |while (i <= 5) {
            |   println(i, fib(i))
            |   i = i + 1
            |}
        """.trimMargin()
        checkOutput(code, """
            |1, 1
            |2, 2
            |3, 3
            |4, 5
            |5, 8
            |
        """.trimMargin())
    }
}