package ru.nsu.jexpression_path.predict_expression

import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.types.PredictOperand

class PredictExpressionExecutor {
    companion object {
        fun execute(predictionOperand: PredictOperand, jExpression: JExpression): Boolean {
            val res = predictionOperand.get(jExpression)

            if (res !is Boolean) throw IllegalArgumentException("Failed to execute predict because it return not boolean type")

            return res
        }
    }
}