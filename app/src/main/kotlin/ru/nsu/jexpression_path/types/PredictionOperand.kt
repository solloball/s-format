package ru.nsu.jexpression_path.types

import ru.nsu.jexpression.JExpression

sealed class PredictionOperand {
    data class Atom(val a: JExpression)
    data class Equal(val a: PredictionOperand, val b: PredictionOperand)
    data class NotEqual(val a: PredictionOperand, val b: PredictionOperand)
    data class Greater(val a: PredictionOperand, val b: PredictionOperand)
    data class GreaterEqual(val a: PredictionOperand, val b: PredictionOperand)
    data class Less(val a: PredictionOperand, val b: PredictionOperand)
    data class LessEqual(val a: PredictionOperand, val b: PredictionOperand)
    data class Empty(val a: JExpression)
    data class Size(val a: JExpression)
    data class Current(val a: JExpression)
}