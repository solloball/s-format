package ru.nsu.jexpression_path.executor

import com.google.common.collect.ImmutableList
import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.enums.Operand
import ru.nsu.jexpression_path.enums.PathMode
import ru.nsu.jexpression_path.types.ParserResult

class JExpressionPathExecutorDumb: JExpressionPathExecutor {
    override fun execute(root: JExpression, parserResult: ParserResult): List<JExpression> {
        return when (parserResult.mode) {
            PathMode.DEEP_MODE -> executeDeep(root, parserResult)
            PathMode.DEFAULT_MODE -> executeDefault(root, parserResult)
        }
    }

    private fun executeDefault(root: JExpression, parserResult: ParserResult): List<JExpression> {
        var result: List<JExpression> = arrayListOf(root)

        for (i in 0..<parserResult.operandValues.size) {
            if (result.isEmpty()) {
                return emptyList()
            }
            result = executeOperand(result, parserResult.operandValues[i])
        }

        return result
    }

    private fun executeDeep(root: JExpression, parserResult: ParserResult): List<JExpression> {
        val res = ArrayList<JExpression>()

        res.addAll(executeDefault(root, parserResult))

        when (root) {
            is JExpression.JArray -> root.getValue().forEach{ res.addAll(executeDeep(it, parserResult)) }
            is JExpression.JObject -> root.getValue().forEach{ res.addAll(executeDeep(it.value, parserResult)) }
            else -> {}
        }

        return res
    }

    private fun executeOperand(list: List<JExpression>, operandValue: ParserResult.OperandValue): List<JExpression> {
        return when (operandValue.operand) {
            Operand.CHILD_DOT -> executeChildDot(list, operandValue.arguments)
            Operand.CHILD_BRACKET -> executeChildBracket(list, operandValue.arguments)
            Operand.ELEMENT_INDEX -> executeElementIndex(list, operandValue.arguments)
            Operand.FILTER_EXPRESSION -> executeFilter(list, operandValue.arguments)
            Operand.CURRENT -> executeCurrent(list)
        }
    }

    private fun executeChildDot(list: List<JExpression>, argument: List<Any>): List<JExpression> {
        val name = argument[0] as String
        return list
            .filterIsInstance<JExpression.JObject>()
            .filter { it.getValue().containsKey(name) }
            .map { it.getValue()[name] as JExpression }
    }

    private fun executeChildBracket(list: List<JExpression>, argument: List<Any>): List<JExpression> {
        val name = argument[0] as String
        return list
            .filterIsInstance<JExpression.JObject>()
            .filter { it.getValue().containsKey(name) }
            .map { it.getValue()[name] as JExpression }
    }

    private fun executeElementIndex(list: List<JExpression>, argument: List<Any>): List<JExpression> {
        val idx = argument[0] as Int
        return list
            .filterIsInstance<JExpression.JArray>()
            .filter { it.getValue().size > idx }
            .map { it.getValue()[idx] }
    }

    private fun executeFilter(list: List<JExpression>, argument: List<Any>): List<JExpression> {
        TODO()
    }

    private fun executeCurrent(list: List<JExpression>): List<JExpression> {
        return list
    }
}