package ru.nsu.jexpression

import ru.nsu.sexpression.SExpression

sealed class JExpression private constructor(private val sExpression: SExpression) {
    abstract fun getValue(): Any
    fun getSExpression(): SExpression {
        return sExpression
    }

    override fun toString(): String {
        return sExpression.toString()
    }

    class JString private constructor(private val sExpression: SExpression.AtomString): JExpression(sExpression = sExpression) {
        override fun getValue(): String {
            return sExpression.value
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.AtomString): JString {
                return JString(sExpression)
            }
        }
    }

    class JInteger private constructor(private val sExpression: SExpression.AtomInteger): JExpression(sExpression = sExpression) {
        override fun getValue(): Int {
            return sExpression.value
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.AtomInteger): JInteger {
                return JInteger(sExpression)
            }
        }
    }

    class JArray private constructor(private val sExpression: SExpression.List) : JExpression(sExpression = sExpression) {
        override fun getValue(): List<JExpression> {
            return sExpression.elements.map { fromSExpression(it) }
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.List): JArray {
                return JArray(sExpression)
            }
        }
    }

    class JObject private constructor(private val sExpression: SExpression.Array) : JExpression(sExpression = sExpression) {
        override fun getValue(): Map<String, JExpression> {
            val res = mutableMapOf<String, JExpression>()

            // Перебор элементов массива, где ключи и значения чередуются
            for (i in 0 until sExpression.elements.size step 2) {
                val key = sExpression.elements[i] as? SExpression.AtomString
                    ?: throw IllegalArgumentException("Expected string key in JObject")

                // Преобразуем значение через fromSExpression в соответствующий объект JExpression
                val value = fromSExpression(sExpression.elements[i + 1])
                res[key.value] = value
            }

            return res
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.Array): JObject {
                if (!validate(sExpression)) {
                    throw IllegalArgumentException("Failed to create instance due to invalid format of object $sExpression")
                }
                return JObject(sExpression)
            }

            private fun validate(sExpression: SExpression.Array): Boolean {
                // Проверка на корректность структуры SExpression.Array
                if (sExpression.elements.isEmpty() || sExpression.elements.size % 2 != 0) {
                    return false
                }
                val keys = mutableSetOf<String>()
                for (i in 0 until sExpression.elements.size step 2) {
                    val key = sExpression.elements[i] as? SExpression.AtomString
                        ?: return false // Ключ должен быть строкой
                    if (keys.contains(key.value)) {
                        return false // Проверка на уникальность ключей
                    }
                    keys.add(key.value)
                }
                return true
            }
        }
    }

    companion object {
        // Метод для конвертации SExpression в JExpression
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