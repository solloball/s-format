package ru.nsu.jexpression_path.predicate_expression

import ru.nsu.jexpression_path.enums.PredicateOperandType
import ru.nsu.jexpression_path.types.PredicateOperand

class PredicateExpressionParser {
    companion object {
        fun parse(input: String): PredicateOperand {
            val trim = input.trim()
            val operand = PredicateOperandType.entries.find { it.rawRegex.matches(trim) }
                ?: throw IllegalArgumentException("Failed to parse predict expression because of illegal syntax")

            return when (operand) {
                PredicateOperandType.EQUAL -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredicateOperand.Equal(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredicateOperandType.NOT_EQUAL -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredicateOperand.NotEqual(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredicateOperandType.GREATER -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredicateOperand.Greater(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredicateOperandType.GREATER_EQUAL -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredicateOperand.GreaterEqual(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredicateOperandType.LESS -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredicateOperand.Less(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredicateOperandType.LESS_EQUAL -> {
                    val matchedGroups = operand.rawRegex.matchEntire(trim)!!.groups
                    PredicateOperand.LessEqual(parse(matchedGroups[1]!!.value), parse(matchedGroups[2]!!.value))
                }

                PredicateOperandType.SIZE -> PredicateOperand.Size
                PredicateOperandType.CURRENT -> PredicateOperand.Current
                PredicateOperandType.ATOM -> {
                    val value = if (input.toIntOrNull() == null) input else input.toInt()
                    PredicateOperand.AtomValue(value)
                }

                PredicateOperandType.EMPTY -> PredicateOperand.Empty
            }
        }
    }
}