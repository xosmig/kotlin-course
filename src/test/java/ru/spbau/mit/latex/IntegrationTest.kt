package ru.spbau.mit.latex

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

class IntegrationTest {
    fun integrationTest(testName: String, file: TexFile) {
        ByteArrayOutputStream().use { baos ->
            file.print(baos)
            val expectedOutput = Files.readAllBytes(Paths.get("src/test/data/$testName.tex"))
            Assert.assertEquals(
                    expectedOutput.toString(Charset.defaultCharset()),
                    baos.toByteArray().toString(Charset.defaultCharset()))
        }
    }

    @Test
    fun helloWorldTest() {
        val file = article {
            document {
                +"Hello world!"
            }
        }
        integrationTest("helloWorld", file)
    }

    @Test
    fun beamerTest() {
        val file = beamerFile("12pt") {
            document {
                frame {
                    title("Hello World Frame")
                    +"Hello World!"
                }
            }
        }
        integrationTest("beamer", file)
    }

    @Test
    fun itemizeTest() {
        val file = article {
            document {
                itemize("arg1", "arg2") {
                    for (row in 1..5) {
                        item { +"row number $row" }
                    }
                }
            }
        }
        integrationTest("itemize", file)
    }

    @Test
    fun enumerateTest() {
        val file = article {
            usepackage("enumerate")
            document {
                enumerate("I") {
                    for (row in 1..5) {
                        item { +"row number $row" }
                    }
                }
            }
        }
        integrationTest("enumerate", file)
    }

    @Test
    fun mathTest() {
        val file = article {
            document {
                math {
                    text { +"a plus b times c:" }
                    +"a + b $times c"
                }
            }
        }
        integrationTest("math", file)
    }

    @Test
    fun alignmentTest() {
        val file = article {
            document {
                flushleft { +"left" }
                flushright { +"right" }
                center { +"center" }
            }
        }
        integrationTest("alignment", file)
    }

    @Test
    fun customTagTest() {
        val file = article {
            document {
                customTextTag("pyglist", "language=kotlin") {
                    center { +"foobar" }
                }
                customMathTag("pyglist", "language=kotlin") {
                    +"foobar $times 5"
                }
            }
        }
        integrationTest("customTag", file)
    }

    @Test
    fun emptyLineTest() {
        val file = article {
            document {
                // Shouldn't have any indentation
                emptyLine()
            }
        }
        integrationTest("emptyLine", file)
    }

    @Test
    fun customFileTest() {
        val file = customFile("my little document type", "12pt") {}
        integrationTest("customFile", file)
    }
}
