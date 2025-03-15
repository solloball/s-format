package ru.nsu.jexpression_path.predicate_expression

import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.types.PredicateOperand

class PredicateExpressionExecutor {
    companion object {
        fun execute(predictionOperand: PredicateOperand, jExpression: JExpression): Boolean {
            val res = predictionOperand.get(jExpression)

            if (res !is Boolean) throw IllegalArgumentException("Failed to execute predict because it return not boolean type")

            return res
        }
    }
}