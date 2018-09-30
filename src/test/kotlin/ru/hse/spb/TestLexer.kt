//package ru.hse.spb
//
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.Test
//import java.nio.file.Files
//import java.nio.file.Paths
//
//class TestLexer {
//
//    @Test(expected = RuntimeException::class)
//    fun testMismatchedBrackets() {
//        val string = "var a = 10)"
//        val lexer = Lexer(listOf(string))
//        val tokens = lexer.getTokens()
//    }
//
//    @Test
//    fun testLexerOnText1() {
//        println(Paths.get(".").normalize().toAbsolutePath())
//        val file = "src/resources/test1"
//        val lines = Files.readAllLines(Paths.get(file))
//        val lexer = Lexer(lines)
//
//        val correctTokens = listOf<Token>(
//                KeyWordToken(KeyWord.VAR),
//                IdentifierToken("a"),
//                KeyWordToken(KeyWord.ASSIGN),
//                LiteralToken(10),
//                KeyWordToken(KeyWord.VAR),
//                IdentifierToken("b"),
//                KeyWordToken(KeyWord.ASSIGN),
//                LiteralToken(20),
//                KeyWordToken(KeyWord.IF),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("a"),
//                KeyWordToken(KeyWord.GT),
//                IdentifierToken("b"),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.LBRACE),
//                IdentifierToken("println"),
//                KeyWordToken(KeyWord.LBRACKET),
//                LiteralToken(1),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.RBRACE),
//                KeyWordToken(KeyWord.ELSE),
//                KeyWordToken(KeyWord.LBRACE),
//                IdentifierToken("println"),
//                KeyWordToken(KeyWord.LBRACKET),
//                LiteralToken(0),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.RBRACE)
//        )
//
//        val tokens: List<Token> = lexer.getTokens().asList()
//        assertThat(tokens.size).isEqualTo(correctTokens.size)
//        for (i: Int in 0 until tokens.size) {
//            val token: Token = tokens[i]
//            val correctToken: Token = correctTokens[i]
//            when (correctToken) {
//                is IdentifierToken -> assertThat(token is IdentifierToken)
//                is KeyWordToken -> assertThat(token is KeyWordToken)
//                is LiteralToken -> assertThat(token is LiteralToken)
//            }
//        }
//    }
//
//    @Test
//    fun testLexerOnText2() {
//        val file = "src/resources/test2"
//        val lines = Files.readAllLines(Paths.get(file))
//        val lexer = Lexer(lines)
//
//        val correctTokens = listOf<Token>(
//                KeyWordToken(KeyWord.FUN),
//                IdentifierToken("fib"),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("n"),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.LBRACE),
//                KeyWordToken(KeyWord.IF),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("n"),
//                KeyWordToken(KeyWord.LE),
//                LiteralToken(1),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.LBRACE),
//                KeyWordToken(KeyWord.RETURN),
//                LiteralToken(1),
//                KeyWordToken(KeyWord.RBRACE),
//                KeyWordToken(KeyWord.RETURN),
//                IdentifierToken("fib"),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("n"),
//                KeyWordToken(KeyWord.SUB),
//                LiteralToken(1),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.ADD),
//                IdentifierToken("fib"),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("n"),
//                KeyWordToken(KeyWord.SUB),
//                LiteralToken(2),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.RBRACE),
//                KeyWordToken(KeyWord.VAR),
//                IdentifierToken("i"),
//                KeyWordToken(KeyWord.ASSIGN),
//                LiteralToken(1),
//                KeyWordToken(KeyWord.WHILE),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("i"),
//                KeyWordToken(KeyWord.LE),
//                LiteralToken(5),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.LBRACE),
//                IdentifierToken("println"),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("i"),
//                KeyWordToken(KeyWord.COMMA),
//                IdentifierToken("fib"),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("i"),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.RBRACKET),
//                IdentifierToken("i"),
//                KeyWordToken(KeyWord.ASSIGN),
//                IdentifierToken("i"),
//                KeyWordToken(KeyWord.ADD),
//                LiteralToken(1),
//                KeyWordToken(KeyWord.RBRACE)
//        )
//
//        val tokens: List<Token> = lexer.getTokens().asList()
//        assertThat(tokens.size).isEqualTo(correctTokens.size)
//        for (i: Int in 0 until tokens.size) {
//            val token: Token = tokens[i]
//            val correctToken: Token = correctTokens[i]
//            when (correctToken) {
//                is IdentifierToken -> assertThat(token is IdentifierToken)
//                is KeyWordToken -> assertThat(token is KeyWordToken)
//                is LiteralToken -> assertThat(token is LiteralToken)
//            }
//        }
//    }
//
//    @Test
//    fun testLexerOnText3() {
//        println(Paths.get(".").normalize().toAbsolutePath())
//        val file = "src/resources/test3"
//        val lines = Files.readAllLines(Paths.get(file))
//        val lexer = Lexer(lines)
//
//        val correctTokens = listOf<Token>(
//                KeyWordToken(KeyWord.FUN),
//                IdentifierToken("foo"),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("n"),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.LBRACE),
//                KeyWordToken(KeyWord.FUN),
//                IdentifierToken("bar"),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("m"),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.LBRACE),
//                KeyWordToken(KeyWord.RETURN),
//                IdentifierToken("m"),
//                KeyWordToken(KeyWord.ADD),
//                IdentifierToken("n"),
//                KeyWordToken(KeyWord.RBRACE),
//                KeyWordToken(KeyWord.RETURN),
//                IdentifierToken("bar"),
//                KeyWordToken(KeyWord.LBRACKET),
//                LiteralToken(1),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.RBRACE),
//                IdentifierToken("println"),
//                KeyWordToken(KeyWord.LBRACKET),
//                IdentifierToken("foo"),
//                KeyWordToken(KeyWord.LBRACKET),
//                LiteralToken(41),
//                KeyWordToken(KeyWord.RBRACKET),
//                KeyWordToken(KeyWord.RBRACKET)
//        )
//
//        val tokens: List<Token> = lexer.getTokens().asList()
//        assertThat(tokens.size).isEqualTo(correctTokens.size)
//        for (i: Int in 0 until tokens.size) {
//            val token: Token = tokens[i]
//            val correctToken: Token = correctTokens[i]
//            when (correctToken) {
//                is IdentifierToken -> assertThat(token is IdentifierToken)
//                is KeyWordToken -> assertThat(token is KeyWordToken)
//                is LiteralToken -> assertThat(token is LiteralToken)
//            }
//        }
//    }
//}