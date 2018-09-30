package ru.hse.spb

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.hse.spb.visitor.Executor
import ru.hse.spb.visitor.FunException
import ru.hse.spb.parser.FunLexer
import ru.hse.spb.parser.FunParser
import ru.hse.spb.parser.FunVisitor
import java.io.OutputStreamWriter
import java.lang.Exception

fun printReturnCode(code: Int) {
    println("finished with exit code $code")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        println("Need path to the FunFile!")
        return
    }
    val file = args[0]

    val lexer = FunLexer(CharStreams.fromFileName(file))
    val parser = FunParser(CommonTokenStream(lexer))

    try {
        val tree = parser.file()
        val visitor: FunVisitor<Int?> = Executor(OutputStreamWriter(System.out))
        visitor.visit(tree)
        printReturnCode(0)
    } catch (e: FunException) {
        System.err.print(e.message)
        printReturnCode(1)
    } catch (e: Exception) {
        System.err.print(e.message)
        printReturnCode(1)
    }
}