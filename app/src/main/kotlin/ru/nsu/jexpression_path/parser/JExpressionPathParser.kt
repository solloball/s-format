package ru.nsu.jexpression_path.parser

import ru.nsu.jexpression_path.types.ParserResult

interface JExpressionPathParser {
    fun parse(jExpressionPath: String): ParserResult
}