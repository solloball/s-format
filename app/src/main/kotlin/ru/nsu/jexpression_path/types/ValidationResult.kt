package ru.nsu.jexpression_path.types

data class ValidationResult( val ok: Boolean, val error: String? ) {
    companion object {
        fun errorResult(error: String): ValidationResult {
            return ValidationResult(false, error)
        }
        fun successResult(): ValidationResult {
            return ValidationResult(true, null)
        }
    }
}
