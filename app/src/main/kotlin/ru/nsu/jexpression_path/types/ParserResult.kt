package ru.nsu.jexpression_path.types

import ru.nsu.jexpression_path.enums.OperandType
import ru.nsu.jexpression_path.enums.PathMode

data class ParserResult(
    val mode: PathMode,
    val operandValues: List<OperandValue>
) {
    data class OperandValue(val operand: OperandType, val arguments: List<Any>)
}
