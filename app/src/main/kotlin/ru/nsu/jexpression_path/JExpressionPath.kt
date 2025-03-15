package ru.nsu.jexpression_path

import com.google.common.collect.ImmutableList
import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.executor.JExpressionPathExecutor
import ru.nsu.jexpression_path.parser.JExpressionPathParser
import ru.nsu.jexpression_path.types.UpdaterResult
import ru.nsu.sexpression.SExpression

class JExpressionPath private constructor(
    private val jExpressionPathParser: JExpressionPathParser,
    private val jExpressionPathExecutor: JExpressionPathExecutor,
) {
    fun find(jExpressionPath: String, root: JExpression): List<JExpression> {

        val parserResult = jExpressionPathParser.parse(jExpressionPath)

        val result = jExpressionPathExecutor.execute(root, parserResult)

        return result
    }

    fun modify(jExpressionPath: String, root: JExpression, newElement: JExpression): UpdaterResult {
        val finderResult = find(jExpressionPath, root)
        if (finderResult.size != 1) {
            return UpdaterResult(false, null)
        }

        return UpdaterResult(true, findAndChange(root, finderResult[0], newElement))
    }

    private fun findAndChange(root: JExpression, oldValue: JExpression, toChange: JExpression): JExpression {
        if (root.getValue() == oldValue.getValue()) {
            return toChange
        }
        return when (root) {
            is JExpression.JArray -> JExpression.JArray.fromSExpression( SExpression.List( root.getValue().map { findAndChange(it, oldValue, toChange).getSExpression() } ) )
            is JExpression.JObject -> JExpression.JObject.fromSExpression( SExpression.Array( root.getValue().flatMap { ImmutableList.of(SExpression.AtomString(it.key), findAndChange(it.value, oldValue, toChange).getSExpression()) } ) )
            is JExpression.JString -> root
            is JExpression.JInteger -> root
        }
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