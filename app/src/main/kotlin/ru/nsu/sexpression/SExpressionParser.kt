package ru.nsu.sexpression

interface SExpressionParser {
    fun parse(str: String): SExpression
}