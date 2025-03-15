package ru.nsu.jexpression_path.types

import ru.nsu.jexpression.JExpression

sealed class PredicateOperand {
    abstract fun get(jExpression: JExpression): Any?

    data class Equal(val a: PredicateOperand, val b: PredicateOperand): PredicateOperand() {
        override fun get(jExpression: JExpression): Any {
            return a.get(jExpression) == b.get(jExpression)
        }

    }

    data class NotEqual(val a: PredicateOperand, val b: PredicateOperand): PredicateOperand() {
        override fun get(jExpression: JExpression): Any {
            return a.get(jExpression) == b.get(jExpression)
        }

    }

    data class Greater(val a: PredicateOperand, val b: PredicateOperand): PredicateOperand() {
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

    data class GreaterEqual(val a: PredicateOperand, val b: PredicateOperand): PredicateOperand() {
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

    data class Less(val a: PredicateOperand, val b: PredicateOperand): PredicateOperand() {
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

    data class LessEqual(val a: PredicateOperand, val b: PredicateOperand): PredicateOperand() {
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

    data object Empty: PredicateOperand() {
        override fun get(jExpression: JExpression): Any? {
            return when (jExpression) {
                is JExpression.JArray -> jExpression.getValue().isEmpty()
                is JExpression.JInteger -> null
                is JExpression.JObject -> jExpression.getValue().isEmpty()
                is JExpression.JString -> jExpression.getValue().isEmpty()
            }
        }
    }

    data object Size : PredicateOperand() {
        override fun get(jExpression: JExpression): Any? {
            return when (jExpression) {
                is JExpression.JArray -> jExpression.getValue().size
                is JExpression.JInteger -> null
                is JExpression.JObject -> jExpression.getValue().size
                is JExpression.JString -> jExpression.getValue().length
            }
        }

    }

    data object Current : PredicateOperand() {
        override fun get(jExpression: JExpression): Any {
            return jExpression.getValue()
        }

    }

    data class AtomValue(val a: Any): PredicateOperand() {
        override fun get(jExpression: JExpression): Any {
            return a
        }
    }
}