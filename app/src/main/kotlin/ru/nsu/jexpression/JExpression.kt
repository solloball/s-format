package ru.nsu.jexpression

import ru.nsu.sexpression.SExpression

sealed class JExpression private constructor(private val sExpression: SExpression) {
    abstract fun getValue(): Any?

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
            val res = HashMap<String, JExpression>()

            for (i in 0..sExpression.elements.size / 2 step 2) {
                val key = sExpression.elements[i] as SExpression.AtomString
                val value = fromSExpression(sExpression.elements[i + 1])
                res[key.value] = value
            }

            return res
        }

        companion object {
            fun fromSExpression(sExpression: SExpression.Array): JObject {
                if (!validate(sExpression)) {
                    throw IllegalArgumentException("Failed to create instance because of invalid format of object $sExpression")
                }
                return JObject(sExpression)
            }

            private fun validate(sExpression: SExpression.Array): Boolean {
                if (sExpression.elements.isEmpty() || sExpression.elements.size % 2 != 0) {
                    return false
                }
                val  keys = HashSet<String>()
                for(i in 0..sExpression.elements.size / 2 step 2) {
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
