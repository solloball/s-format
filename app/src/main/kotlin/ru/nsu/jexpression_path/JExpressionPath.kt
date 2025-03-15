package ru.nsu.jexpression_path

import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.executor.JExpressionPathExecutor
import ru.nsu.jexpression_path.parser.JExpressionPathParser

class JExpressionPath private constructor(
    private val jExpressionPathParser: JExpressionPathParser,
    private val jExpressionPathExecutor: JExpressionPathExecutor,
) {
    fun find(jExpressionPath: String, root: JExpression): List<JExpression> {

        val parserResult = jExpressionPathParser.parse(jExpressionPath)

        val result = jExpressionPathExecutor.execute(root, parserResult)

        return result
    }

    fun modify(jExpressionPath: String, root: JExpression, newElement: JExpression): List<JExpression> {

        TODO()
    }

    class Builder() {
        private lateinit var jExpressionPathParser: JExpressionPathParser
        private lateinit var jExpressionPathExecutor: JExpressionPathExecutor

        fun parser(jExpressionPathParser: JExpressionPathParser) = apply {
            this.jExpressionPathParser = jExpressionPathParser
        }
        fun executor(jExpressionPathExecutor: JExpressionPathExecutor) = apply {
            this.jExpressionPathExecutor = jExpressionPathExecutor
        }

        fun build() = JExpressionPath(jExpressionPathParser, jExpressionPathExecutor)
    }
}