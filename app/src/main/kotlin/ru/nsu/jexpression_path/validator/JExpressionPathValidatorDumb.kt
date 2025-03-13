package ru.nsu.jexpression_path.validator

import ru.nsu.jexpression_path.types.ValidationResult

class JExpressionPathValidatorDumb: JExpressionPathValidator {
    override fun validate(jExpressionPath: String): ValidationResult {
        return ValidationResult.successResult()
    }
}