package ru.hse.spb

// problem 51B: http://codeforces.com/contest/51/submission/42777201

class TableParser {

    private var listOfSizes = mutableListOf<Int>()
    private var string = ""

    fun getTableSizes(text: String): List<Int> {
        listOfSizes = mutableListOf()
        string = text.replace("\n", "")
        parseTable()
        return listOfSizes.sorted()
    }

    private fun parseTable() {
        var cells = 0
        while (string.isNotEmpty()) {
            when {
                string.startsWith("<table>") -> {
                    removeTag("<table>")
                    parseTable()
                }
                string.startsWith("</table>") -> {
                    listOfSizes.add(cells)
                    removeTag("</table>")
                    return
                }
                string.startsWith("<td>") -> {
                    removeTag("<td>")
                    cells++
                }
                string.startsWith("<tr>") -> {
                    removeTag("<tr>")
                }
                string.startsWith("</tr>") -> {
                    removeTag("</tr>")
                }
                string.startsWith("</td>") -> {
                    removeTag("</td>")
                }
                else -> return
            }
        }
    }

    private fun removeTag(tag: String) {
        string = string.removePrefix(tag)
    }
}

private fun readTable(): String {
    var table = ""
    var line = readLine()
    while (!line.isNullOrBlank()) {
        table += line
        line = readLine()
    }
    return table
}

private fun run() {
    val parser = TableParser()
    val listOfSizes = parser.getTableSizes(readTable())
    println(listOfSizes.joinToString(separator = " "))
}

fun main(args: Array<String>) {
    run()
}