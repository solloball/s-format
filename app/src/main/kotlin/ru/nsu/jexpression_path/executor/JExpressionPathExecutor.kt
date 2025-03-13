package ru.nsu.jexpression_path.executor

import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.types.ParserResult

interface JExpressionPathExecutor {
    fun execute(root: JExpression, parserResult: ParserResult): List<JExpression>
}