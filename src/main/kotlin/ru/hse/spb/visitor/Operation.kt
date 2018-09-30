package ru.hse.spb.visitor

fun Boolean.toInt() = if (this) 1 else 0

class Operation(str: String) {

    private val operator: Operator = when (str) {
        "+" -> Operator.PLUS
        "-" -> Operator.MINUS
        "*" -> Operator.MULT
        "/" -> Operator.DIV
        "%" -> Operator.MOD
        ">" -> Operator.GT
        "<" -> Operator.LT
        ">=" -> Operator.GE
        "<=" -> Operator.LE
        "==" -> Operator.EQ
        "!=" -> Operator.NEQ
        "&&" -> Operator.AND
        "||" -> Operator.OR
        else -> throw UnsupportedOperationException(this.javaClass.canonicalName)
    }

    enum class Operator {
        PLUS, MINUS, MULT, DIV, MOD,
        GT, LT, GE, LE, EQ, NEQ,
        AND, OR
    }

    fun apply(left: Int, right: Int): Int? {
        val l = (left == 1)
        val r = (right == 1)
        return when(operator) {
            Operator.MULT -> left * right
            Operator.DIV -> left / right
            Operator.MOD -> left % right
            Operator.PLUS -> left + right
            Operator.MINUS -> left - right
            Operator.GT -> (left > right).toInt()
            Operator.LT -> (left < right).toInt()
            Operator.GE -> (left >= right).toInt()
            Operator.LE -> (left <= right).toInt()
            Operator.EQ -> (left == right).toInt()
            Operator.NEQ -> (left != right).toInt()
            Operator.AND -> (l && r).toInt()
            Operator.OR -> (l || r).toInt()
        }
    }
}