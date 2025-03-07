package ru.nsu.jexpression.parser

import ru.nsu.jexpression.JExpression
import ru.nsu.sexpression.SExpressionParser

class JExpressionParserDumb(private val sExpressionParser: SExpressionParser): JExpressionParser {
    override fun fromString(str: String): JExpression {
        return JExpression.fromSExpression(sExpressionParser.parse(str))
    }
}