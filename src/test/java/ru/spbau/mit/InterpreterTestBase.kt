package ru.spbau.mit

import org.junit.Assert
import java.io.ByteArrayOutputStream

open class InterpreterTestBase {
    protected fun checkOutput(ast: File, expectedOutput: String) {
        val stdout = ByteArrayOutputStream()
        ast.perform(stdout)
        Assert.assertArrayEquals(expectedOutput.toByteArray(), stdout.toByteArray())
    }
}
