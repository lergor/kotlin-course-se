package ru.hse.spb

// problem 51B: http://codeforces.com/contest/51/submission/43508055

fun getTableSizes(text: String): List<Int> {
    val string = StringBuffer(text.replace("\n", ""))
    return parseTable(string).sorted()
}

private fun parseTable(tableString: StringBuffer): List<Int> {
    var cells = 0
    val listOfSizes = mutableListOf<Int>()

    while (tableString.isNotEmpty()) {
        when {
            tableString.advanceIf("<table>") -> {
                listOfSizes.addAll(parseTable(tableString))
            }
            tableString.advanceIf("</table>") -> {
                listOfSizes.add(cells)
                return listOfSizes
            }
            tableString.advanceIf("<td>") -> {
                cells++
            }
            tableString.advanceIf("<tr>") -> {
            }
            tableString.advanceIf("</tr>") -> {
            }
            tableString.advanceIf("</td>") -> {
            }
            else -> return listOfSizes
        }
    }

    return listOfSizes
}

private fun StringBuffer.advanceIf(tag: String): Boolean {
    return this.startsWith(tag).also { starts ->
        if (starts) delete(0, tag.length)
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
    val listOfSizes = getTableSizes(readTable())
    println(listOfSizes.joinToString(separator = " "))
}

fun main(args: Array<String>) {
    run()
}
