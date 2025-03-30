
package ru.nsu.sexpression

import org.junit.jupiter.api.Assertions.*

class SExpressionParserDumbTest

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import ru.nsu.sexpression.SExpression
import ru.nsu.sexpression.SExpressionParserDumb

class SExpressionParserDumbTest {

    private val parser = SExpressionParserDumb()

    @Test
    fun `test parse atom string`() {
        val input = "hello"
        val result = parser.parse(input)

        assertTrue(result is SExpression.AtomString)
        assertEquals("hello", (result as SExpression.AtomString).value)
    }

    @Test
    fun `test parse atom integer`() {
        val input = "123"
        val result = parser.parse(input)

        assertTrue(result is SExpression.AtomInteger)
        assertEquals(123, (result as SExpression.AtomInteger).value)
    }

    @Test
    fun `test parse list`() {
        val input = "(hello 123)"
        val result = parser.parse(input)

        assertTrue(result is SExpression.List)
        val list = result as SExpression.List
        assertEquals(2, list.elements.size)
        assertTrue(list.elements[0] is SExpression.AtomString)
        assertEquals("hello", (list.elements[0] as SExpression.AtomString).value)
        assertTrue(list.elements[1] is SExpression.AtomInteger)
        assertEquals(123, (list.elements[1] as SExpression.AtomInteger).value)
    }

    @Test
    fun `test parse array`() {
        val input = "[hello 123]"
        val result = parser.parse(input)

        assertTrue(result is SExpression.Array)
        val array = result as SExpression.Array
        assertEquals(2, array.elements.size)
        assertTrue(array.elements[0] is SExpression.AtomString)
        assertEquals("hello", (array.elements[0] as SExpression.AtomString).value)
        assertTrue(array.elements[1] is SExpression.AtomInteger)
        assertEquals(123, (array.elements[1] as SExpression.AtomInteger).value)
    }

    @Test
    fun `test parse nested list`() {
        val input = "(hello (world 456) 789)"
        val result = parser.parse(input)

        assertTrue(result is SExpression.List)
        val list = result as SExpression.List
        assertEquals(3, list.elements.size)

        assertTrue(list.elements[0] is SExpression.AtomString)
        assertEquals("hello", (list.elements[0] as SExpression.AtomString).value)

        assertTrue(list.elements[1] is SExpression.List)
        val nestedList = list.elements[1] as SExpression.List
        assertTrue(nestedList.elements[0] is SExpression.AtomString)
        assertEquals("world", (nestedList.elements[0] as SExpression.AtomString).value)
        assertTrue(nestedList.elements[1] is SExpression.AtomInteger)
        assertEquals(456, (nestedList.elements[1] as SExpression.AtomInteger).value)

        assertTrue(list.elements[2] is SExpression.AtomInteger)
        assertEquals(789, (list.elements[2] as SExpression.AtomInteger).value)
    }

    @Test
    fun `test parse empty list`() {
        val input = "()"
        val result = parser.parse(input)

        assertTrue(result is SExpression.List)
        val list = result as SExpression.List
        assertTrue(list.elements.isEmpty())
    }

    @Test
    fun `test parse empty array`() {
        val input = "[]"
        val result = parser.parse(input)

        assertTrue(result is SExpression.Array)
        val array = result as SExpression.Array
        assertTrue(array.elements.isEmpty())
    }

    @Test
    fun `test parse list with spaces and newlines`() {
        val input = "(  hello   \n  123  )"
        val result = parser.parse(input)

        assertTrue(result is SExpression.List)
        val list = result as SExpression.List
        assertEquals(2, list.elements.size)
        assertTrue(list.elements[0] is SExpression.AtomString)
        assertEquals("hello", (list.elements[0] as SExpression.AtomString).value)
        assertTrue(list.elements[1] is SExpression.AtomInteger)
        assertEquals(123, (list.elements[1] as SExpression.AtomInteger).value)
    }
}

