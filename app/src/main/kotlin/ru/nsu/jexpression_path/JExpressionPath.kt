package ru.nsu.jexpression_path

import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.executor.JExpressionPathExecutor
import ru.nsu.jexpression_path.parser.JExpressionPathParser
import ru.nsu.jexpression_path.validator.JExpressionPathValidator

class JExpressionPath private constructor(
    private val jExpressionPathParser: JExpressionPathParser,
    private val jExpressionPathValidator: JExpressionPathValidator,
    private val jExpressionPathExecutor: JExpressionPathExecutor,
) {
    fun find(jExpressionPath: String, root: JExpression): List<JExpression> {
        validate(jExpressionPath)

        val parserResult = jExpressionPathParser.parse(jExpressionPath)

        val result = jExpressionPathExecutor.execute(root, parserResult)

        return result
    }

    fun modify(jExpressionPath: String, root: JExpression, newElement: JExpression): List<JExpression> {
        validate(jExpressionPath)

        TODO()
    }

    private fun validate(jExpressionPath: String) {
        val validationRes = jExpressionPathValidator.validate(jExpressionPath)
        if (!validationRes.ok) throw IllegalArgumentException(validationRes.error)
    }

    data class Builder(
        var jExpressionPathParser: JExpressionPathParser,
        var jExpressionPathValidator: JExpressionPathValidator,
        var jExpressionPathExecutor: JExpressionPathExecutor
    ) {
        fun parser(jExpressionPathParser: JExpressionPathParser) = apply {
            this.jExpressionPathParser = jExpressionPathParser
        }
        fun validator(jExpressionPathValidator: JExpressionPathValidator) = apply {
            this.jExpressionPathValidator = jExpressionPathValidator
        }
        fun validator(jExpressionPathExecutor: JExpressionPathExecutor) = apply {
            this.jExpressionPathExecutor = jExpressionPathExecutor
        }
        fun build() = JExpressionPath(jExpressionPathParser, jExpressionPathValidator, jExpressionPathExecutor)
    }
}