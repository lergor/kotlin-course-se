package ru.hse.spb

class Lexer(private val lines: List<String>) {

    private var tokens: MutableList<Token> = mutableListOf()
    private var currentLine = 0
    private var positionInLine = 0
    private var braces = 0
    private var brackets = 0

    fun getTokens(): List<Token> {
        clearLexer()
        while (!endOfProgram()) processLine()
        checkBrackets()
        return tokens
    }

    private fun clearLexer() {
        currentLine = 0
        positionInLine = 0
        braces = 0
        brackets = 0
        tokens = mutableListOf()
    }

    fun endOfLine() = positionInLine >= line().length

    fun endOfProgram() = currentLine >= lines.size

    private fun checkBrackets() {
        if (braces % 2 != 0 || brackets % 2 != 0) throw RuntimeException("mismatched brackets")
    }

    private fun processLine() {
        positionInLine = 0
        var token: Token
        while (!endOfLine()) {
            token = nextToken()
            checkAndAdd(token)
        }
        currentLine += 1
    }

    private fun countBrackets(token: Token) {
        if (token is KeyWordToken) {
            when (token.keyWord) {
                KeyWord.LBRACE -> braces++
                KeyWord.RBRACE -> braces--
                KeyWord.LBRACKET -> brackets++
                KeyWord.RBRACKET -> brackets--
                else -> {
                }
            }
            if (braces < 0 || brackets < 0) throw RuntimeException("mismatched brackets")
        }
    }

    private fun checkAndAdd(token: Token) {
        countBrackets(token)
        if (token !is IdentifierToken || token.string.isNotEmpty()) tokens.add(token)
    }

    private fun skipSpacesAndComments() {
        if (line().startsWith("//", positionInLine)) positionInLine = line().length
        while (!endOfLine() && symbol().isWhitespace()) positionInLine += 1
    }

    private fun symbol() = line()[positionInLine]

    private fun symbolAndMovePos() = line()[positionInLine++]

    private fun line() = lines[currentLine]

    private fun nextToken(): Token {
        skipSpacesAndComments()
        var result = readWordOrNumber()
        if (result.isNotEmpty()) {
            return when {
                stringToToken.containsKey(result) -> KeyWordToken(stringToToken.getValue(result))
                result.toIntOrNull() != null -> LiteralToken(result.toInt())
                else -> IdentifierToken(result)
            }
        }
        result = readKeySymbols()
        if (result.isNotEmpty()) return KeyWordToken(stringToToken.getValue(result))
        return IdentifierToken("")
    }

    private fun readKeySymbols(): String {
        skipSpacesAndComments()
        var result = ""
        while (!endOfLine() && !symbol().isWhitespace()) {
            when {
                (result.isNotEmpty() && stringToToken.containsKey(result.last().toString() + symbol().toString())) -> return result.last() + symbolAndMovePos().toString()
                (result.isNotEmpty() && stringToToken.containsKey(result.last().toString())) -> return result
                else -> result += symbolAndMovePos()
            }
        }
        return result
    }

    private fun readWordOrNumber(): String {
        var result = ""
        while (!endOfLine() && !symbol().isWhitespace() && symbol().isLetterOrDigit()) {
            result += symbolAndMovePos()
        }
        return result
    }

}