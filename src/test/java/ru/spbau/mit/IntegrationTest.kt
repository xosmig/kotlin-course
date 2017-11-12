package ru.spbau.mit

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths



class IntegrationTest {
    private fun integrationTest(testName: String) {
        val stdout = ByteArrayOutputStream()
        interpretFile("src/test/data/$testName.hw", stdout)
        val fileLocation = Paths.get("src/test/data/$testName.out")
        val expectedOutput = Files.readAllBytes(fileLocation)
        Assert.assertArrayEquals(expectedOutput, stdout.toByteArray())
    }

    private fun recordResult(testName: String) {
        val stdout = FileOutputStream("src/test/data/$testName.out")
        interpretFile("src/test/data/$testName.hw", stdout)
    }

    @Test
    fun helloWorldTest() {
//        recordResult("helloWorld")
        integrationTest("helloWorld")
    }

    @Test
    fun fibonacciTest() {
//        recordResult("fibonacci")
        integrationTest("fibonacci")
    }

    @Test
    fun innerFunctionTest() {
//        recordResult("returnExample")
        integrationTest("returnExample")
    }

    @Test
    fun associativityTest() {
//        recordResult("returnExample")
        integrationTest("associativity")
    }
}
