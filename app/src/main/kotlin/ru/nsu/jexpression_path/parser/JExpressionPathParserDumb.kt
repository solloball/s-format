package ru.nsu.jexpression_path.parser

import com.google.common.base.Strings
import ru.nsu.jexpression_path.enums.Operand
import ru.nsu.jexpression_path.enums.PathMode
import ru.nsu.jexpression_path.types.ParserResult

class JExpressionPathParserDumb: JExpressionPathParser {
    override fun parse(jExpressionPath: String): ParserResult {
        if (jExpressionPath.isEmpty()) {
            throw IllegalArgumentException("Failed to parse jExpressionPath because of illegal input string size")
        }

        val mode: PathMode = PathMode.fromPath(jExpressionPath)

        val operandValues = ArrayList<ParserResult.OperandValue>()
        var path = jExpressionPath.replaceFirst(mode.jExpressionPrefix, "")

        while (path.isNotEmpty()) {
            val operand = Operand.entries.find { it.fullRegex().matches(path) }
                ?: throw IllegalArgumentException("Failed to parse jExpressionPath because failed to find operand: $path")

            val matchedPath = operand.fullRegex().matchEntire(path)
                ?: throw IllegalArgumentException("Failed to parse because of illegal regex: $path")

            if (matchedPath.groupValues.size - 1 != operand.argumentCount()) {
                throw  IllegalArgumentException("Failed to parse because of illegal count of groupValues: $path")
            }

            val reg = operand.shortRegex()

            val values: List<Any> = operand.argumentTypes().mapIndexed { index, kClass ->
                val rawValue = matchedPath.groupValues[index + 1]
                when (kClass) {
                    String::class -> rawValue
                    Int::class -> rawValue.toIntOrNull()
                        ?: throw IllegalArgumentException("Failed to parse because of illegal int: $rawValue")
                    else -> throw IllegalArgumentException("Not implemented")
                }
            }

            operandValues.add(ParserResult.OperandValue(operand, values))
            path = path.replaceFirst(operand.shortRegex(), "")

        }

        return ParserResult(mode, operandValues)
    }
}