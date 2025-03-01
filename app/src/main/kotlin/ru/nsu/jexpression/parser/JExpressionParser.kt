package ru.nsu.jexpression.parser

import ru.nsu.jexpression.JExpression

interface JExpressionParser {
    fun fromString(str: String): JExpression
}