package ru.nsu.sexpression

class SExpressionParserDumb: SExpressionParser {
    private var pos = 0
    private var input: String = ""
    private val skippedSymbol = listOf(' ', '\n')
    private val invalidAtomValues = listOf(' ', '(', ')', '\n', '[', ']')

    override fun parse(str: String): SExpression {
        input = str
        pos = 0

        return parseInternal()
    }

    private fun parseInternal(): SExpression {
        skipCharacters()

        return when (peek()) {
            '(' -> parseList()
            '[' -> parseArray()
            else -> parseAtom()
        }
    }

    private fun parseList(): SExpression.List {
        assertSymbol('(')
        next()

        val elements = mutableListOf<SExpression>()
        while (peek() != ')') {
            elements.add(parseInternal())
            skipCharacters()
        }

        assertSymbol(')')
        next()

        return SExpression.List(elements)
    }

    private fun parseArray(): SExpression.Array {
        assertSymbol('[')
        next()

        val elements = mutableListOf<SExpression>()
        while (peek() != ']') {
            elements.add(parseInternal())
            skipCharacters()
        }

        assertSymbol(']')
        next()

        return SExpression.Array(elements)
    }

    private fun parseAtom(): SExpression {
        val sb = StringBuilder()
        while (peek().let { it != null && !invalidAtomValues.contains(it) }) {
            sb.append(next())
        }

        val value = sb.toString()
        return if (value.toIntOrNull() != null) SExpression.AtomInteger(value.toInt()) else  SExpression.AtomString(value)
    }

    private fun skipCharacters() {
        while (skippedSymbol.contains(peek())) {
            next()
        }
    }

    private fun assertSymbol(symbol: Char?) {
        if (peek() != symbol) {
            throw IllegalArgumentException("Failed to parse s-expression. Expected '$symbol'")
        }
    }

    private fun peek(): Char? = if (pos < input.length) input[pos] else null

    private fun next(): Char = input[pos++]
}