package ru.spbau.mit

import org.junit.Test

class InterpreterTest : InterpreterTestBase() {

    @Test
    fun helloWorldTest() {
        val ast = File(Block(listOf(
                FunctionCall("println", listOf(
                        Literal(43110), Literal(111)
                ))
        )))
        checkOutput(ast, """
            |43110, 111
            |
        """.trimMargin())
    }

    @Test
    fun fibTest() {
        val ast = File(Block(listOf(
                Function("fib", listOf("n"), Block(listOf(
                        If(LessEqual(Reference("n"), Literal(1)), Block(listOf(
                                Return(Literal(1))
                        ))),
                        Return(Plus(
                                FunctionCall("fib", listOf(Minus(Reference("n"), Literal(1)))),
                                FunctionCall("fib", listOf(Minus(Reference("n"), Literal(2))))
                        )
                )))),
                Variable("i", Literal(1)),
                While (LessEqual(Reference("i"), Literal(5)), Block(listOf(
                        FunctionCall("println", listOf(
                                Reference("i"),
                                FunctionCall("fib", listOf(Reference("i")))
                        )),
                        Assignment("i", Plus(Reference("i"), Literal(1)))
                )))
        )))
        checkOutput(ast, """
            |1, 1
            |2, 2
            |3, 3
            |4, 5
            |5, 8
            |
        """.trimMargin())
    }
}
