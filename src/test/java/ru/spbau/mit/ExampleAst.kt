package ru.spbau.mit

object ExampleAst {
    val helloWorldAst = File(Block(listOf(
            Variable("a", Literal(10)),
            Variable("b", Literal(20)),
            If(BinaryOperation(Reference("a"), ">", Reference("b")), Block(listOf(
                    FunctionCall("println", listOf(Literal(1)))
            )), Block(listOf(
                    FunctionCall("println", listOf(Literal(0)))
            )))
    )))

    val fibonacci = File(Block(listOf(
            Function("fib", listOf("n"), Block(listOf(
                    If(BinaryOperation(Reference("n"), "<=", Literal(1)), Block(listOf(
                            Return(Literal(1))
                    ))),
                    Return(BinaryOperation(
                            FunctionCall("fib", listOf(BinaryOperation(Reference("n"), "-", Literal(1)))),
                            "+",
                            FunctionCall("fib", listOf(BinaryOperation(Reference("n"), "-", Literal(2))))
                    ))
            ))),
            Variable("i", Literal(1)),
            While (BinaryOperation(Reference("i"), "<=", Literal(5)), Block(listOf(
                    FunctionCall("println", listOf(
                            Reference("i"),
                            FunctionCall("fib", listOf(Reference("i")))
                    )),
                    Assignment("i", BinaryOperation(Reference("i"), "+", Literal(1)))
            )))
    )))

    val returnExample = File(Block(listOf(
            Function("foo", listOf("n"), Block(listOf(
                    Function("bar", listOf("m"), Block(listOf(
                            Return(BinaryOperation(Reference("m"), "+", Reference("n")))
                    ))),
                    Return(FunctionCall("bar", listOf(Literal(1))))
            ))),
            FunctionCall("println", listOf(FunctionCall("foo", listOf(Literal(41)))))
    )))
}
