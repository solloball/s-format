package ru.nsu.jexpression_path.predict_expression

import ru.nsu.jexpression.JExpression
import ru.nsu.jexpression_path.types.PredictionOperand

// TODO: implement it.
interface PredictExpressionExecutor {
    fun execute(predictionOperand: PredictionOperand, jExpression: JExpression): Boolean
}