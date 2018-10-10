package ru.hse.spb.tex

import kotlin.test.assertEquals
import org.junit.Test

class TestTex {

    @Test
    fun documentClass() {
        val result = document {
            documentClass("documentclass")
        }.toString()

        assertEquals("""
            |\documentclass{documentclass}
            |""".trimMargin(), result)
    }

    @Test
    fun usePackage() {
        val result = document {
            usepackage("package1")
            usepackage("package2", "package3")
            usepackage("package", "param1" to "opt1", "param2" to "opt2")
        }.toString()

        assertEquals("""
            |\usepackage{package1}
            |\usepackage{package2, package3}
            |\usepackage[param1=opt1][param2=opt2]{package}
            |""".trimMargin(), result)
    }

    @Test
    fun title() {
        val result = document {
            title("title1")
        }.toString()

        assertEquals("""
            |\title{title1}
            |""".trimMargin(), result)
    }

    @Test
    fun author() {
        val result = document {
            author("author1")
        }.toString()

        assertEquals("""
            |\author{author1}
            |""".trimMargin(), result)
    }

    @Test
    fun date() {
        val result = document {
            date("date123")
        }.toString()

        assertEquals("""
            |\date{date123}
            |""".trimMargin(), result)
    }

    @Test
    fun itemize() {
        val result = document {
            documentBody {
                itemize("param1" to "opt1") {
                    for (item in 1..3) {
                        item {
                            +"kek $item"
                        }
                    }
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{itemize}[param1=opt1]
            |\item
            |kek 1
            |\item
            |kek 2
            |\item
            |kek 3
            |\end{itemize}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun enumerate() {
        val result = document {
            documentBody {
                enumerate {
                    for (item in 1..3) {
                        item {
                            +"kek $item"
                        }
                    }
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{enumerate}
            |\item
            |kek 1
            |\item
            |kek 2
            |\item
            |kek 3
            |\end{enumerate}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun frame() {
        val result = document {
            documentBody {
                frame("KEKFrame", "param1" to "opt1", "param2" to "opt2") {
                    +"kek"
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{frame}[param1=opt1][param2=opt2]
            |\frametitle{KEKFrame}
            |kek
            |\end{frame}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun customTag() {
        val result = document {
            documentBody {
                frame("KEKFrame", "param1" to "opt1") {
                    customTag("one", listOf("two"), "param2" to "opt2") {
                        +"kek1"
                        +"kek2"
                        +"kek3"
                    }
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{frame}[param1=opt1]
            |\frametitle{KEKFrame}
            |\begin{one}{two}[param2=opt2]
            |kek1
            |kek2
            |kek3
            |\end{one}
            |\end{frame}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun math() {
        val result = document {
            documentBody {
                math {
                    +"\\psi(x) \\to \\exists(x) \\psi(x)"
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{displaymath}
            |\psi(x) \to \exists(x) \psi(x)
            |\end{displaymath}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun alignment() {
        val result = document {
            documentBody {
                right {
                    +"right kek"
                }
            }
        }.toString()

        assertEquals("""
            |\begin{document}
            |\begin{right}
            |right kek
            |\end{right}
            |\end{document}
            |""".trimMargin(), result)
    }

    @Test
    fun simpleDocument() {
        val result = document {
            documentClass("beamer")
            usepackage("package1", "param1" to "opt1")
            title("Super KEK document")
            author("thats me")
            date("today")
            title("KEK document")
            documentBody {
                +"document content"
            }
        }.toString()

        assertEquals("""
            |\documentclass{beamer}
            |\usepackage[param1=opt1]{package1}
            |\title{Super KEK document}
            |\author{thats me}
            |\date{today}
            |\title{KEK document}
            |\begin{document}
            |document content
            |\end{document}
            |""".trimMargin(), result)
    }
}