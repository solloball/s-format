package ru.nsu.jschema

import ru.nsu.jexpression.JExpression

sealed class JSchema {
    abstract fun validate(expression: JExpression): Boolean

    object JStringSchema : JSchema() {
        override fun validate(expression: JExpression): Boolean = expression is JExpression.JString
    }

    object JIntegerSchema : JSchema() {
        override fun validate(expression: JExpression): Boolean = expression is JExpression.JInteger
    }

    class JArraySchema(private val itemSchema: JSchema) : JSchema() {
        override fun validate(expression: JExpression): Boolean {
            return expression is JExpression.JArray && expression.getValue().all { itemSchema.validate(it) }
        }
    }

    class JObjectSchema(private val properties: Map<String, JSchema>, private val required: Set<String> = emptySet()) : JSchema() {
        override fun validate(expression: JExpression): Boolean {
            if (expression !is JExpression.JObject) return false
            val obj = expression.getValue()

            println("Validating object: $obj")

            if (!required.all { it in obj }) {
                println("Missing required fields: ${required.filter { it !in obj }}")
                return false
            }

            return obj.all { (key, value) ->
                val schema = properties[key]
                val isValid = schema?.validate(value) ?: false
                if (!isValid) {
                    println("Field '$key' failed validation.") // 👀 Какое поле не прошло проверку?
                }
                isValid
            }
        }
    }

    companion object {
        fun validateDocument(expression: JExpression, schema: JSchema): Boolean {
            return schema.validate(expression)
        }
    }
}
