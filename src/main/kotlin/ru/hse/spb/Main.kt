package ru.hse.spb

import org.antlr.v4.runtime.BufferedTokenStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.ParseCancellationException
import ru.hse.spb.ErrorListener
import ru.hse.spb.parser.FunLexer
import ru.hse.spb.parser.FunParser
import ru.hse.spb.parser.FunVisitor
import ru.hse.spb.FunEvalVisitor
import java.io.OutputStreamWriter

fun main(args: Array<String>) {
//    if (args.size != 1) {
//        println("Need path to the file!")
//        return
//    }

//    val fileName = args[0]
    val fileName = "/home/valeriya/Desktop/AU/III/JVM/kotlin-course-se/src/main/resources/test2"
    val funLexer = FunLexer(CharStreams.fromFileName(fileName))
//    funLexer.removeErrorListeners()
//    funLexer.addErrorListener(ErrorListener())

    val funParser = FunParser(BufferedTokenStream(funLexer))
//    funParser.removeErrorListeners()
//    funParser.addErrorListener(ErrorListener())

    try {
        val tree = funParser.file()
        val visitor: FunVisitor<Int?> = FunEvalVisitor(OutputStreamWriter(System.out))
        visitor.visit(tree)
        printReturnCode(0)
    } catch (e: ParseCancellationException) {
        System.err.print(e.message)
        System.err.flush()
        printReturnCode(1)
    } catch (e: Exception) {
        System.err.print(e.message)
        printReturnCode(1)
    }
}

fun printReturnCode(code: Int) {
    println("\nFunProgram finished with exit code " + code)
}