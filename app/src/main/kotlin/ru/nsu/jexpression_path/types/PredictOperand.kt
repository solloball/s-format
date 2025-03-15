package ru.nsu.jexpression_path.types

import ru.nsu.jexpression.JExpression

sealed class PredictOperand {
    abstract fun get(jExpression: JExpression): Any?

    data class Equal(val a: PredictOperand, val b: PredictOperand): PredictOperand() {
        override fun get(jExpression: JExpression): Any {
            return a.get(jExpression) == b.get(jExpression)
        }

    }

    data class NotEqual(val a: PredictOperand, val b: PredictOperand): PredictOperand() {
        override fun get(jExpression: JExpression): Any {
            return a.get(jExpression) == b.get(jExpression)
        }

    }

    data class Greater(val a: PredictOperand, val b: PredictOperand): PredictOperand() {
        override fun get(jExpression: JExpression): Any {
            val aValue = a.get(jExpression)
            val bValue = b.get(jExpression)
            if (aValue is Number && bValue is Number) {
                return aValue.toInt() > bValue.toInt()
            }
            if (aValue is String && bValue is String) {
                return aValue > bValue
            }
            return false
        }

    }

    data class GreaterEqual(val a: PredictOperand, val b: PredictOperand): PredictOperand() {
        override fun get(jExpression: JExpression): Any {
            val aValue = a.get(jExpression)
            val bValue = b.get(jExpression)
            if (aValue is Number && bValue is Number) {
                return aValue.toInt() >= bValue.toInt()
            }
            if (aValue is String && bValue is String) {
                return aValue >= bValue
            }
            return false
        }

    }

    data class Less(val a: PredictOperand, val b: PredictOperand): PredictOperand() {
        override fun get(jExpression: JExpression): Any {
            val aValue = a.get(jExpression)
            val bValue = b.get(jExpression)
            if (aValue is Number && bValue is Number) {
                return aValue.toInt() < bValue.toInt()
            }
            if (aValue is String && bValue is String) {
                return aValue < bValue
            }
            return false
        }

    }

    data class LessEqual(val a: PredictOperand, val b: PredictOperand): PredictOperand() {
        override fun get(jExpression: JExpression): Any {
            val aValue = a.get(jExpression)
            val bValue = b.get(jExpression)
            if (aValue is Number && bValue is Number) {
                return aValue.toInt() <= bValue.toInt()
            }
            if (aValue is String && bValue is String) {
                return aValue <= bValue
            }
            return false
        }

    }

    data object Empty: PredictOperand() {
        override fun get(jExpression: JExpression): Any? {
            return when (jExpression) {
                is JExpression.JArray -> jExpression.getValue().isEmpty()
                is JExpression.JInteger -> null
                is JExpression.JObject -> jExpression.getValue().isEmpty()
                is JExpression.JString -> jExpression.getValue().isEmpty()
            }
        }
    }

    data object Size : PredictOperand() {
        override fun get(jExpression: JExpression): Any? {
            return when (jExpression) {
                is JExpression.JArray -> jExpression.getValue().size
                is JExpression.JInteger -> null
                is JExpression.JObject -> jExpression.getValue().size
                is JExpression.JString -> jExpression.getValue().length
            }
        }

    }

    data object Current : PredictOperand() {
        override fun get(jExpression: JExpression): Any {
            return jExpression.getValue()
        }

    }

    data class AtomValue(val a: Any): PredictOperand() {
        override fun get(jExpression: JExpression): Any {
            return a
        }
    }
}