package ru.hse.spb

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.hse.spb.parser.FunLexer
import ru.hse.spb.parser.FunParser
import org.junit.Test
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.assertj.core.api.Assertions.assertThat

class TestParser {

    private val sep = "\n"

    private fun getParser(program: String): FunParser {
        val lexer = FunLexer(CharStreams.fromString(program))
        val parser = FunParser(CommonTokenStream(lexer))
        return parser
    }

    @Test
    fun operation() {
        val program = "17 + 12"
        assertNotNull(getParser(program).expression())
    }

    @Test
    fun identifier() {
        val program = "funfun"
        assertNotNull(getParser(program).expression())
    }

    @Test
    fun literal() {
        val program = "1111"
        assertNotNull(getParser(program).expression())
    }

    @Test
    fun variableDeclaration() {
        val program = "var i" + sep
        val parser = getParser(program)
        val variableDeclaration = parser.variableDeclaration()
        assertNotNull(variableDeclaration)
        assertThat(variableDeclaration.Identifier().text).isEqualTo("i")
        assertNull(variableDeclaration.expression())
    }

    @Test
    fun variableDeclarationAndDefinition() {
        val program = "var i = 10" + sep
        val parser = getParser(program)
        val variableDeclaration = parser.variableDeclaration()
        assertNotNull(variableDeclaration)
        assertThat(variableDeclaration.Identifier().text).isEqualTo("i")
        assertNotNull(variableDeclaration.expression())
    }

    @Test
    fun functionWithNoArgsNoBody() {
        val code = "fun foo() {" + sep +
                    "}" + sep
        val parser = getParser(code)
        val functionDeclaration = parser.functionDeclaration()
        assertNotNull(functionDeclaration)
        assertThat(functionDeclaration.Identifier().text).isEqualTo("foo")
        assertThat(functionDeclaration.parameterNames().Identifier().size).isEqualTo(0)
        assertThat(functionDeclaration.blockWithBraces().block().statement().size).isEqualTo(0)
    }

    @Test
    fun functionWithArgsAndBody() {
        val code = "fun foo(a, b) {" + sep +
                   "    println(a)"  + sep +
                    "}"              + sep
        val parser = getParser(code)
        val functionDeclaration = parser.functionDeclaration()
        assertNotNull(functionDeclaration)
        assertThat(functionDeclaration.Identifier().text).isEqualTo("foo")
        assertThat(functionDeclaration.parameterNames().Identifier().size).isEqualTo(2)
        assertThat(functionDeclaration.parameterNames().Identifier(0).toString()).isEqualTo("a")
        assertThat(functionDeclaration.parameterNames().Identifier(1).toString()).isEqualTo("b")
        assertThat(functionDeclaration.blockWithBraces().block().statement().size).isEqualTo(1)
    }

    @Test
    fun whileStatement() {
        val program =   "var i = 0"           + sep +
                        "while (i < 10) {"    + sep +
                        "   i = i + 1"        + sep +
                        "}"                   + sep
        val parser = getParser(program)
        val whileStatement = parser.file().block().statement(1).whileStatement()
        assertNotNull(whileStatement)
        assertNotNull(whileStatement.expression())
        assertNotNull(whileStatement.blockWithBraces())
        assertThat(whileStatement.blockWithBraces().block().statement().size).isEqualTo(1)
    }

    @Test
    fun ifStatement() {
        val program =   "if(a < b) {"     + sep +
                        "   println(1)"   + sep +
                        "}"               + sep
        val parser = getParser(program)
        val ifStatement = parser.ifStatement()
        assertNotNull(ifStatement)
        assertNotNull(ifStatement.expression())
        assertNotNull(ifStatement.blockWithBraces())
        assertThat(ifStatement.blockWithBraces().size).isEqualTo(1)
    }

    @Test
    fun ifElseStatement() {
        val program =   "if(a < b) {"     + sep +
                        "   println(1)"   + sep +
                        "} else {"        + sep +
                        "   println(2)"   + sep +
                        "}"               + sep
        val parser = getParser(program)
        val ifStatement = parser.ifStatement()
        assertNotNull(ifStatement)
        assertNotNull(ifStatement.expression())
        assertNotNull(ifStatement.blockWithBraces())
        assertThat(ifStatement.blockWithBraces().size).isEqualTo(2)
    }

    @Test
    fun assignment() {
        val program =   "var i"     + sep +
                        "i = 10"    + sep
        val parser = getParser(program)
        val statements = parser.file().block().statement()
        assertNotNull(statements)
        assertThat(statements.size).isEqualTo(2)
        assertThat(statements[1].assignmentStatement().Identifier().toString()).isEqualTo("i")
        assertNotNull(statements[1].assignmentStatement().expression())
    }

    @Test
    fun returnStatement() {
        val program = "return 0" + sep
        val parser = getParser(program)
        val returnStatement = parser.returnStatement()
        assertNotNull(returnStatement)
        assertNotNull(returnStatement.expression())
    }

    @Test
    fun functionCall() {
        val program = "foo(10)" + sep
        val parser = getParser(program)
        val functionCall = parser.functionCallExpression()
        assertNotNull(functionCall)
        assertThat(functionCall.Identifier().toString()).isEqualTo("foo")
        assertThat(functionCall.arguments().expression().size).isEqualTo(1)
    }

    @Test
    fun mismatchedBrackets() {
        val incorrectProgram = "fun foo {"  + sep +
                                "}"         + sep
        val parser = getParser(incorrectProgram)
        val functionDeclaration = parser.functionDeclaration()
        assertNotNull(functionDeclaration)
        assertThat(functionDeclaration.Identifier().toString()).isEqualTo("foo")
        assertNull(functionDeclaration.parameterNames())
        assertNull(functionDeclaration.blockWithBraces())
    }
}