package ru.spbau.mit.latex

import org.junit.Assert.assertArrayEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.test.assertEquals

class IntegrationTest {
    private fun integrationTest(testName: String, tree: ) {
        val stdout = ByteArrayOutputStream()

        val expectedOutput = Files.readAllBytes(Paths.get("src/test/data/$testName.tex"))
        Assert.assertArrayEquals(expectedOutput, stdout.toByteArray())
    }

    @Test
    fun helloWorldTest() {

        ByteArrayOutputStream().use { baos ->
            customFile("article", "12pt") {
                document {
                    +"Hello world!"
                }
            }.print(baos)
            assertArrayEquals()
        }

//        \documentclass{article}[12pt]
//        \begin{document}
//        Hello world!
//        $Hello world!$ %math mode
//        \end{document}
    }
}
