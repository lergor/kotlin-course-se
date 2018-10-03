package ru.hse.spb

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.hse.spb.parser.FunLexer
import ru.hse.spb.parser.FunParser
import ru.hse.spb.parser.FunVisitor
import java.io.StringWriter
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.*
import ru.hse.spb.visitor.*

class TestExecutor {

    private val sep = "\n"

    private fun executeProgram(program: String, writer: StringWriter): Int? {
        val lexer = FunLexer(CharStreams.fromString(program))
        val parser = FunParser(CommonTokenStream(lexer))
        val tree = parser.file()
        val visitor: FunVisitor<Int?> = Executor(writer)
        return visitor.visit(tree)
    }

    @Test
    fun additionOperations() {
        val program =
                "println(17 + 12)" + sep +
                "println(17 - 12)" + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        val results = writer.buffer.split(sep).filter { s -> s.isNotEmpty() }
        assertThat(results).containsOnlyElementsOf(listOf("29", "5"))
    }

    @Test
    fun multiplicationOperations() {
        val program =
                "println(3 * 3)" + sep +
                "println(3 / 3)" + sep +
                "println(2 % 3)" + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        val results = writer.buffer.split(sep).filter { s -> s.isNotEmpty() }
        assertThat(results).containsOnlyElementsOf(listOf("9", "1", "2"))
    }

    @Test
    fun equalityOperations() {
        val program =
                "println(2 == 3)" + sep +
                "println(2 == 2)" + sep +
                "println(2 != 2)" + sep +
                "println(2 != 3)" + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        val results = writer.buffer.split(sep).filter { s -> s.isNotEmpty() }
        assertThat(results).containsOnlyElementsOf(listOf("0", "1", "0", "1"))
    }

    @Test
    fun relationOperations() {
        val program =
                "println(2 > 3)"    + sep +
                "println(3 > 2)"    + sep +
                "println(3 < 2)"    + sep +
                "println(2 < 3)"    + sep +
                "println(2 >= 3)"   + sep +
                "println(3 >= 2)"   + sep +
                "println(3 <= 2)"   + sep +
                "println(2 <= 3)"   + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        val results = writer.buffer.split(sep).filter { s -> s.isNotEmpty() }
        assertThat(results).containsOnlyElementsOf(listOf(  "0", "1",
                                                            "0", "1",
                                                            "0", "1",
                                                            "0", "1"))
    }

    @Test
    fun logicalOperations() {
        val program =
                    "println((2 > 3) || (3 > 2))" + sep +
                    "println((2 > 3) || (1 > 2))" + sep +
                    "println((2 > 3) && (3 > 2))" + sep +
                    "println((3 > 2) && (3 > 1))" + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        val results = writer.buffer.split(sep).filter { s -> s.isNotEmpty() }
        assertThat(results).containsOnlyElementsOf(listOf(  "1", "0",
                                                            "0", "1"))
}

    @Test
    fun negativeNumber() {
        val program = "println(-17)"
        val writer = StringWriter()
        executeProgram(program, writer)
        assertThat(writer.buffer.split(sep)[0]).isEqualTo("-17")
    }

    @Test
    fun whileStatement() {
        val program =   "var i = 0"           + sep +
                        "while (i < 10) {"    + sep +
                        "   i = i + 1"        + sep +
                        "}"                   + sep +
                        "println(i)"          + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        assertEquals("10", writer.toString().replace(sep, ""))
    }

    @Test
    fun returnStatement() {
        val program = "return 1"
        val writer = StringWriter()
        val res = executeProgram(program, writer)
        assertEquals(1, res)
    }

    @Test
    fun ifStatement() {
        val program =
                "var i = 5"         + sep +
                "if (i < 10) {"     + sep +
                "    i = i + 1"     + sep +
                "}"                 + sep +
                "println(i)"        + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        assertEquals("6", writer.toString().replace(sep, ""))
    }

    @Test
    fun ifWithElseStatement() {
        val program =
                "var i = 0"         + sep +
                "if (i > 10) {"     + sep +
                "    i = i + 1"     + sep +
                "} else {"          + sep +
                "    i = i + 2"     + sep +
                "}"                 + sep +
                "println(i)"        + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        assertEquals("2", writer.toString().replace(sep, ""))
    }

    @Test
    fun functionDeclarationStatement() {
        val program =
                "fun fib(n) {"                          + sep +
                "    if (n <= 1) {"                     + sep +
                "        return 1"                      + sep +
                "    }"                                 + sep +
                "    return fib(n - 1) + fib(n - 2)"    + sep +
                "}"                                     + sep +
                "println(fib(5))"                       + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        assertEquals("8", writer.toString().replace(sep, ""))
    }

    @Test
    fun variableDeclarationStatement() {
        val programVarWithValue =
                        "var i = 9"     + sep +
                        "println(i)"    + sep
        val writer1 = StringWriter()
        executeProgram(programVarWithValue, writer1)
        assertEquals("9", writer1.toString().replace(sep, ""))

        val programVarWithoutValue =
                "var i"         + sep +
                "i = 10"        + sep +
                "println(i)"    + sep
        val writer2 = StringWriter()
        executeProgram(programVarWithoutValue, writer2)
        assertEquals("10", writer2.toString().replace(sep, ""))
    }


    @Test
    fun functionCall() {
        val program =
                "fun fact(n) {"                 + sep +
                "    if (n == 0) {"             + sep +
                "        return 1"              + sep +
                "    }"                         + sep +
                "    return n * fact(n - 1)"    + sep +
                "}"                             + sep +
                "println(fact(5))"              + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        assertEquals("120", writer.toString().replace(sep, ""))
    }

    @Test
    fun functionWithoutReturn() {
        val program =
                "fun foo() {"       + sep +
                "    var i = 17"     + sep +
                "}"                 + sep +
                "println(foo())"    + sep
        val writer = StringWriter()
        executeProgram(program, writer)
        assertEquals("0", writer.toString().replace(sep, ""))
    }


    @Test(expected = FunctionNotFound::class)
    fun undeclaredFunctionStatement() {
        val program = "println(foo())"
        val writer = StringWriter()
        executeProgram(program, writer)
    }

    @Test(expected = VariableRedeclarationException::class)
    fun variableRedeclaration() {
        val program =
                "var a = 2" + sep +
                "var a = 3" + sep
        executeProgram(program, StringWriter())
    }

    @Test(expected = FunctionRedeclarationException::class)
    fun functionRedeclaration() {
        val program =
                "fun foo() {"       + sep +
                "    var i = 17"    + sep +
                "}"                 + sep +
                "fun foo() {"       + sep +
                "    var i = 17"    + sep +
                "}"                 + sep
        executeProgram(program, StringWriter())
    }

    @Test(expected = VariableNotFound::class)
    fun undeclaredVariableStatement() {
        val programVarWithoutValue = "i = 10"
        executeProgram(programVarWithoutValue, StringWriter())
    }

    @Test(expected = NotDefinedException::class)
    fun undefinedVariableStatement() {
        val programVarWithoutValue =
                "var a"         + sep +
                "println(a)"    + sep
        executeProgram(programVarWithoutValue, StringWriter())
    }

}