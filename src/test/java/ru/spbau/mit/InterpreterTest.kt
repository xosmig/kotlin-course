package ru.spbau.mit

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream

class InterpreterTest {

    private fun checkOutput(ast: File, expectedOutput: String) {
        val stdout = ByteArrayOutputStream()
        ast.perform(stdout)
        Assert.assertArrayEquals(expectedOutput.toByteArray(), stdout.toByteArray())
    }

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
    fun recursionTest() {
        checkOutput(ExampleAst.fibonacci, """
            |1, 1
            |2, 2
            |3, 3
            |4, 5
            |5, 8
            |
        """.trimMargin())
    }

    @Test
    fun functionCallContextTest1() {
        val ast = File(Block(listOf(
                Function("foo", listOf("a", "b"), Block(listOf(
                        FunctionCall("println", listOf(Reference("a"), Reference("b")))
                ))),
                Variable("a", Literal(2)),
                FunctionCall("foo", listOf(Literal(1), Reference("a")))
        )))
        // Possible wrong output: 2, 2
        checkOutput(ast, """
            |1, 2
            |
        """.trimMargin())
    }

    @Test
    fun functionCallContextTest2() {

    }

    @Test
    fun declarationOrderTest() {
        // One should be able to reference a variable which is declared later in the same file
        val ast = File(Block(listOf(
                Function("foo", listOf(), Block(listOf(
                        FunctionCall("println", listOf(Reference("a")))
                ))),
                Variable("a", Literal(1)),
                FunctionCall("foo", listOf())
        )))
        // Possible wrong output: error
        checkOutput(ast, """
            |1
            |
        """.trimMargin())
    }

    @Test(expected = UnknownVariableException::class)
    fun wrongContextTest() {
        val ast = File(Block(listOf(
                Function("foo", listOf(), Block(listOf(
                        FunctionCall("println", listOf(Reference("a")))
                ))),
                Function("bar", listOf(), Block(listOf(
                        Variable("a", Literal(1)),
                        FunctionCall("foo", listOf())
                ))),
                FunctionCall("bar", listOf())
        )))
        // Possible wrong output: 1
        checkOutput(ast, "")
    }

    @Test
    fun indirectRecursionTest() {
        val ast = File(Block(listOf(
                Function("foo", listOf("a"), Block(listOf(
                        If(Reference("a"), Block(listOf(
                                FunctionCall("println", listOf(Reference("a")))
                        )), Block(listOf(
                                FunctionCall("bar", listOf(Reference("a")))
                        )))
                ))),
                Function("bar", listOf("a"), Block(listOf(
                        FunctionCall("foo", listOf(BinaryOperation(Reference("a"), "+", Literal(3))))
                ))),
                FunctionCall("foo", listOf(Literal(0)))
        )))
        // Possible wrong output: error
        checkOutput(ast, "3\n")
    }

    @Test
    fun shadowingTest() {
        val ast = File(Block(listOf(
                Variable("a", Literal(3)),
                Function("foo", listOf(), Block(listOf(
                        Variable("a", Literal(1)),
                        FunctionCall("println", listOf(Reference("a")))
                ))),
                Function("bar", listOf("a"), Block(listOf(
                        FunctionCall("println", listOf(Reference("a"))),
                        Assignment("a", Literal(-1))
                ))),
                FunctionCall("foo", listOf()),
                FunctionCall("bar", listOf(Literal(2))),
                FunctionCall("println", listOf(Reference("a")))
        )))
        // Possible wrong output: 0 or error
        checkOutput(ast, "1\n2\n3\n")
    }

    @Test(expected = VariableRedeclarationException::class)
    fun variableRedeclarationTest() {
        val ast = File(Block(listOf(
                Variable("a", Literal(1)),
                Variable("a", Literal(2))
        )))
        // Possible wrong output: non error
        checkOutput(ast, "")
    }

    @Test(expected = FunctionRedeclarationException::class)
    fun functionRedeclarationTest() {
        // overloading is not supported
        val ast = File(Block(listOf(
                Function("foo", listOf(), Block(listOf())),
                Function("foo", listOf("a"), Block(listOf()))
        )))
        // Possible wrong output: non error
        checkOutput(ast, "")
    }

    @Test(expected = VariableRedeclarationException::class)
    fun parameterShadowingTest() {
        // overloading is not supported
        val ast = File(Block(listOf(
                Function("foo", listOf("a"), Block(listOf(
                        Variable("a")
                ))),
                FunctionCall("foo", listOf(Literal(1)))
        )))
        // Possible wrong output: non error
        checkOutput(ast, "")
    }

    @Test(expected = WrongNumberOfArgsException::class)
    fun wrongNumberOfArgsTest() {
        val ast = File(Block(listOf(
                Function("foo", listOf("a"), Block(listOf())),
                FunctionCall("foo", listOf(Literal(1), Literal(2)))
        )))
        // Possible wrong output: non error
        checkOutput(ast, "")
    }

    @Test(expected = UnsupportedBinaryOperation::class)
    fun unsupportedBinaryOperationTest() {
        val ast = File(Block(listOf(
                BinaryOperation(Literal(1), "+++", Literal(2))
        )))
        // Possible wrong output: non error
        checkOutput(ast, "")
    }
}
