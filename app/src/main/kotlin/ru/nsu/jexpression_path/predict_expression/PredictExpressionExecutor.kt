package ru.nsu.jexpression_path.predict_expression

import ru.nsu.jexpression.JExpression

// TODO: implement it.
interface PredictExpressionExecutor {
    fun execute(jExpression: JExpression): Boolean
}