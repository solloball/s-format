package ru.nsu.jexpression_path.types

import com.google.common.collect.ImmutableList
import ru.nsu.jexpression_path.enums.Operand
import ru.nsu.jexpression_path.enums.PathMode

data class ParserResult(
    val mode: PathMode,
    val operandValues: List<OperandValue>
) {
    data class OperandValue(val operand: Operand, val arguments: List<Any>)
}
