package ru.nsu.sexpression

sealed class SExpression {
    data class AtomString(val value: String) : SExpression() {
        override fun toString(): String {
            return value
        }
    }
    data class AtomInteger(val value: Int) : SExpression() {
        override fun toString(): String {
            return value.toString()
        }
    }
    data class List(val elements: kotlin.collections.List<SExpression>) : SExpression() {
        override fun toString(): String {
            return " ( " + elements.joinToString(" ") + " ) "
        }
    }
    data class Array(val elements: kotlin.collections.List<SExpression>) : SExpression() {
        override fun toString(): String {
            return "[ " + elements.joinToString(" ") + " ] "
        }
    }
}