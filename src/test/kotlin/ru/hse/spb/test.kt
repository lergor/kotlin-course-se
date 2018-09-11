package ru.hse.spb

import org.junit.Assert.assertEquals
import org.junit.Test

class TestSource {

    @Test
    fun test1() {
        val string = "<table><tr><td></td></tr></table>"
        val parser = TableParser()
        val listOfSizes = parser.getTableSizes(string)
        assertEquals(listOf(1), listOfSizes)
    }

    @Test
    fun test2() {
        val string = "<table>\n" +
                "<tr>\n" +
                "<td>\n" +
                "<table><tr><td></td></tr><tr><td></\n" +
                "td\n" +
                "></tr><tr\n" +
                "><td></td></tr><tr><td></td></tr></table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</table>"
        val parser = TableParser()
        val listOfSizes = parser.getTableSizes(string)
        assertEquals(listOf(1, 4), listOfSizes)
    }

    @Test
    fun test3() {
        val string = "<table><tr><td>\n" +
                "<table><tr><td>\n" +
                "<table><tr><td>\n" +
                "<table><tr><td></td><td></td>\n" +
                "</tr><tr><td></td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr></table>\n" +
                "</td></tr></table>"
        val parser = TableParser()
        val listOfSizes = parser.getTableSizes(string)
        assertEquals(listOf(1, 1, 1, 3), listOfSizes)
    }
}