package ru.nsu.jschema

import ru.nsu.jexpression.JExpression

sealed class JSchema {

    object StringType : JSchema() // Ожидаем строку
    object IntegerType : JSchema() // Ожидаем число
    data class ArrayType(val elementSchema: JSchema) : JSchema() // Ожидаем массив элементов одного типа
    data class ObjectType(val properties: Map<String, JSchema>, val allowExtraFields: Boolean = false) : JSchema() {

        // Метод для валидации объекта
        override fun validate(expr: JExpression): Boolean {
            if (expr !is JExpression.JObject) return false

            val (_, objMap) = expr.getValue()

            // Проверяем, что все обязательные поля присутствуют
            if (!properties.keys.all { it in objMap }) return false

            // Проверяем, что все значения соответствуют их типам
            if (!properties.all { (key, schema) ->
                    objMap[key]?.let { value ->
                        schema.validate(value) // Проверка с использованием схемы для каждого поля
                    } ?: false
                }) return false

            // Проверяем, что дополнительные поля не присутствуют, если они не разрешены
            if (!allowExtraFields && objMap.keys.any { it !in properties }) return false

            return true
        }
    }

    // Универсальный метод для валидации
    open fun validate(expr: JExpression): Boolean {
        return when (this) {
            is StringType -> expr is JExpression.JString
            is IntegerType -> expr is JExpression.JInteger
            is ArrayType -> expr is JExpression.JArray && expr.getValue().all { elementSchema.validate(it) }
            is ObjectType -> (this as ObjectType).validate(expr)
        }
    }
}