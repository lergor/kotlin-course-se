package ru.hse.spb

import ru.hse.spb.tex.document

fun result() =
            document {
                documentClass("beamer")
                usepackage("babel", "russian" /* varargs */)
                documentBody {
                    frame(frameTitle = "frametitle", options = *arrayOf("arg1" to "arg2")) {
                        itemize {
                            for (row in listOf("kek1", "kek2", "kek3")) {
                                item { + "$row text" }
                            }
                        }
                        customTag(name = "pyglist", options = *arrayOf("language" to "kotlin")) {
                            +
                            """
                        |val a = 1
                        """.trimMargin()
                        }
                    }
                }

            }

fun main(args: Array<String>) {
    result().toOutputStream(System.out)
}