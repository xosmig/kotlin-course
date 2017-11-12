package ru.spbau.mit

import org.junit.Test
import kotlin.test.assertEquals

class ParserTest {
    fun parserTest(testName: String, expected: File) {
        val res = Parser.parseFile("src/test/data/$testName.hw")
                .toString()
                .replace(Regex("line=\\d+"), "line=0")
        assertEquals(res, expected.toString())
    }

    @Test
    fun helloWorldTest() {
        parserTest("helloWorld", ExampleAst.helloWorldAst)
    }

    @Test
    fun fibonacciTest() {
        parserTest("fibonacci", ExampleAst.fibonacci)
    }

    @Test
    fun returnExampleTest() {
        parserTest("returnExample", ExampleAst.returnExample)
    }
}
