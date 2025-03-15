package ru.nsu.jexpression_path.executor

import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.enums.OperandType
import ru.nsu.jexpression_path.enums.PathMode
import ru.nsu.jexpression_path.predicate_expression.PredicateExpressionExecutor
import ru.nsu.jexpression_path.types.ParserResult
import ru.nsu.jexpression_path.types.PredicateOperand
import javax.annotation.concurrent.Immutable

class JExpressionPathExecutor {
    fun execute(root: JExpression, parserResult: ParserResult): List<JExpression> {
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
            OperandType.CHILD_DOT -> executeChildDot(list, operandValue.arguments)
            OperandType.CHILD_BRACKET -> executeChildBracket(list, operandValue.arguments)
            OperandType.ELEMENT_INDEX -> executeElementIndex(list, operandValue.arguments)
            OperandType.FILTER_EXPRESSION -> executeFilter(list, operandValue.arguments)
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
        val operand = argument[0] as PredicateOperand

        return list.flatMap{ when(it) {
            is JExpression.JArray -> it.getValue().filter { jExpression -> PredicateExpressionExecutor.execute(operand, jExpression) }
            is JExpression.JInteger -> if (PredicateExpressionExecutor.execute(operand, it)) arrayListOf(it) else emptyList()
            is JExpression.JObject -> it.getValue().values.filter { jExpression -> PredicateExpressionExecutor.execute(operand, jExpression)  }
            is JExpression.JString -> if (PredicateExpressionExecutor.execute(operand, it)) arrayListOf(it) else emptyList()
        } }
    }
}