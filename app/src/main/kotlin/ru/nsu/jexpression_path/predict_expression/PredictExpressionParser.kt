package ru.nsu.jexpression_path.predict_expression

import ru.nsu.jexpression_path.enums.PredictOperandType
import ru.nsu.jexpression_path.types.PredictOperand

class PredictExpressionParser {
    companion object {
        fun parse(input: String): PredictOperand {
            val trim = input.trim()
            val operand = PredictOperandType.entries.find { it.rawRegex.matches(trim) }
                ?: throw IllegalArgumentException("Failed to parse predict expression because of illegal syntax")

            return when (operand) {
                PredictOperandType.EQUAL -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredictOperand.Equal(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredictOperandType.NOT_EQUAL -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredictOperand.NotEqual(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredictOperandType.GREATER -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredictOperand.Greater(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredictOperandType.GREATER_EQUAL -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredictOperand.GreaterEqual(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredictOperandType.LESS -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredictOperand.Less(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredictOperandType.LESS_EQUAL -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredictOperand.LessEqual(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredictOperandType.SIZE -> PredictOperand.Size
                PredictOperandType.CURRENT -> PredictOperand.Current
                PredictOperandType.ATOM -> {
                    val value = if (input.toIntOrNull() == null) input else input.toInt()
                    PredictOperand.AtomValue(value)
                }

                PredictOperandType.EMPTY -> PredictOperand.Empty
            }
        }
    }
}