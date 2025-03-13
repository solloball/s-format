package ru.nsu.jexpression_path.validator

import ru.nsu.jexpression_path.types.ValidationResult

interface JExpressionPathValidator {
    fun validate(jExpressionPath: String): ValidationResult
}