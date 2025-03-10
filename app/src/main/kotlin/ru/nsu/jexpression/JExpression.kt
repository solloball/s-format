package ru.nsu.jexpression

import ru.nsu.sexpression.SExpression

sealed class JExpression private constructor(private val sExpression: SExpression) {
    abstract fun getValue(): Any?

    override fun toString(): String {
        return sExpression.toString()
    }

    class JString private constructor(private val sExpression: SExpression.AtomString) : JExpression(sExpression) {
        override fun getValue(): String {
            return sExpression.value
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.AtomString): JString {
                return JString(sExpression)
            }
        }
    }

    class JInteger private constructor(private val sExpression: SExpression.AtomInteger) : JExpression(sExpression) {
        override fun getValue(): Int {
            return sExpression.value
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.AtomInteger): JInteger {
                return JInteger(sExpression)
            }
        }
    }

    class JArray private constructor(private val sExpression: SExpression.List) : JExpression(sExpression) {
        override fun getValue(): List<JExpression> {
            return sExpression.elements.map { fromSExpression(it) }
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.List): JArray {
                return JArray(sExpression)
            }
        }
    }

    class JObject private constructor(private val sExpression: SExpression.Array) : JExpression(sExpression) {
        override fun getValue(): Pair<String, Map<String, JExpression>> {
            val res = mutableMapOf<String, JExpression>()

            // Считываем элементы с шагом 2, начиная с индекса 1 (ключ)
            for (i in 1 until sExpression.elements.size step 2) {
                val key = sExpression.elements[i] as? SExpression.AtomString
                val value = sExpression.elements[i + 1]

                // Проверяем, что ключ существует и добавляем пару ключ-значение
                if (key != null) {
                    res[key.value] = JExpression.fromSExpression(value)
                } else {
                    throw IllegalArgumentException("Invalid key in object: $key")
                }
            }

            // Возвращаем имя объекта и карту с его полями
            return Pair(sExpression.elements[0].toString(), res)
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.Array): JObject {
                if (!validate(sExpression)) {
                    throw IllegalArgumentException("Failed to create instance because of invalid format of object $sExpression")
                }
                return JObject(sExpression)
            }

            // Проверяем валидность структуры объекта
            private fun validate(sExpression: SExpression.Array): Boolean {
                if (sExpression.elements.isEmpty()
                    || sExpression.elements[0] !is SExpression.AtomString
                    || sExpression.elements.size % 2 == 0
                ) {
                    return false
                }
                val keys = HashSet<String>()
                for (i in 1 until sExpression.elements.size step 2) {
                    val value = sExpression.elements[i]
                    if (value !is SExpression.AtomString || keys.contains(value.value)) {
                        return false
                    }
                    keys.add(value.value)
                }
                return true
            }
        }
    }

    companion object {
        fun fromSExpression(sExpression: SExpression): JExpression {
            return when (sExpression) {
                is SExpression.Array -> JObject.fromSExpression(sExpression)
                is SExpression.AtomInteger -> JInteger.fromSExpression(sExpression)
                is SExpression.AtomString -> JString.fromSExpression(sExpression)
                is SExpression.List -> JArray.fromSExpression(sExpression)
            }
        }
    }
}