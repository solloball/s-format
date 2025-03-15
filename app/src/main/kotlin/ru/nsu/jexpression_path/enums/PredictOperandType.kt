package ru.nsu.jexpression_path.enums

enum class PredictOperandType(val rawRegex: Regex) {
    EQUAL(Regex("\\((.*)\\) == \\((.*)\\)")),
    NOT_EQUAL(Regex("\\((.*)\\) != \\((.*)\\)")),
    GREATER(Regex("\\((.*)\\) > \\((.*)\\)")),
    GREATER_EQUAL(Regex("\\((.*)\\) >= \\((.*)\\)")),
    LESS(Regex("\\((.*)\\) < \\((.*)\\)")),
    LESS_EQUAL(Regex("\\((.*)\\) <= \\((.*)\\)")),
    SIZE(Regex("size\\((.*)\\)")),
    CURRENT(Regex("\\$")),
    EMPTY(Regex("empty\\((.*)\\)")),
    ATOM(Regex("(.*)"));
}