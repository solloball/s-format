package ru.nsu.jexpression_path.enums

import com.google.common.collect.ImmutableList
import ru.nsu.jexpression_path.types.PredictOperand
import kotlin.reflect.KClass

enum class OperandType(private val rawRegex: String, private val argumentTypes: List<KClass<*>>) {
    // TODO: fix for all string
    CHILD_DOT("\\.([a-zA-Z]+)", ImmutableList.of(String::class)),
    CHILD_BRACKET("\\[([a-zA-Z]+)\\]", ImmutableList.of(String::class)),
    ELEMENT_INDEX("\\[([0-9]+)\\]", ImmutableList.of(Int::class)),
    FILTER_EXPRESSION("\\[\\?(.+)\\]", ImmutableList.of(PredictOperand::class)),
    CURRENT("\\$", emptyList());

    fun argumentTypes(): List<KClass<*>> {
        return this.argumentTypes
    }

    fun argumentCount(): Int {
        return this.argumentTypes.size
    }

    fun shortRegex(): Regex {
        return Regex(this.rawRegex)
    }

    fun fullRegex(): Regex {
        return Regex("${this.rawRegex}.*")
    }
}