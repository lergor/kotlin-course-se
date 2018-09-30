package ru.hse.spb

abstract class Token

data class LiteralToken(val int: Int) : Token()

data class IdentifierToken(val string: String) : Token()

data class KeyWordToken(val keyWord: KeyWord) : Token()

enum class KeyWord {
    LBRACE, RBRACE, COMMA, FUN, VAR, LBRACKET, RBRACKET, WHILE, IF, ELSE, ASSIGN, RETURN,
    ADD, SUB, MUL, DIV, MOD,
    GT, LT, GE, LE, EQ, NEQ, OR, AND
}

val stringToToken = mapOf(
        "{" to KeyWord.LBRACE,
        "}" to KeyWord.RBRACE,
        "," to KeyWord.COMMA,
        "fun" to KeyWord.FUN,
        "var" to KeyWord.VAR,
        "(" to KeyWord.LBRACKET,
        ")" to KeyWord.RBRACKET,
        "while" to KeyWord.WHILE,
        "if" to KeyWord.IF,
        "else" to KeyWord.ELSE,
        "=" to KeyWord.ASSIGN,
        "return" to KeyWord.RETURN,
        "+" to KeyWord.ADD,
        "-" to KeyWord.SUB,
        "*" to KeyWord.MUL,
        "/" to KeyWord.DIV,
        "%" to KeyWord.MOD,
        ">" to KeyWord.GT,
        "<" to KeyWord.LT,
        ">=" to KeyWord.LE,
        "<=" to KeyWord.LE,
        "==" to KeyWord.EQ,
        "!=" to KeyWord.NEQ,
        "||" to KeyWord.OR,
        "&&" to KeyWord.AND
)