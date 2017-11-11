package ru.spbau.mit

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import java.io.ByteArrayOutputStream

class TreeInterpreterTest {
    @Test
    fun helloWorldTest() {
        val stdout = ByteArrayOutputStream()
        File(Block(listOf(
                PrintlnCall(listOf(
                        Literal(43110), Literal(111)
                ))
        ))).perform(stdout)
        val expected = """
            |43110, 111
            |
        """.trimMargin()
        assertArrayEquals(expected.toByteArray(), stdout.toByteArray())
    }

    @Test
    fun fibTest() {
        val stdout = ByteArrayOutputStream()
        File(Block(listOf(
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
                        PrintlnCall(listOf(
                                Reference("i"),
                                FunctionCall("fib", listOf(Reference("i")))
                        )),
                        Assignment("i", Plus(Reference("i"), Literal(1)))
                )))
        ))).perform(stdout)
        val expected = """
            |1, 1
            |2, 2
            |3, 3
            |4, 5
            |5, 8
            |
        """.trimMargin()
        assertArrayEquals(expected.toByteArray(), stdout.toByteArray())
    }
}
