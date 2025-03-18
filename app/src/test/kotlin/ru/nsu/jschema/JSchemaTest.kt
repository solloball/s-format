package ru.nsu.jschema

import org.junit.jupiter.api.Test
import ru.nsu.jexpression.JExpression
import ru.nsu.sexpression.SExpression

class JSchemaTest {
    private val stringSchema = JSchema.JStringSchema
    private val intSchema = JSchema.JIntegerSchema
    private val arraySchema = JSchema.JArraySchema(intSchema)
    private val objectSchema = JSchema.JObjectSchema(
        mapOf("name" to stringSchema, "age" to intSchema),
        setOf("name")
    )

    @Test
    fun testStringValidation() {
        val validString = JExpression.JString.fromSExpression(SExpression.AtomString("Hello"))
        kotlin.test.assertTrue(JSchema.validateDocument(validString, stringSchema))
    }

    @Test
    fun testIntegerValidation() {
        val validInt = JExpression.JInteger.fromSExpression(SExpression.AtomInteger(42))
        kotlin.test.assertTrue(JSchema.validateDocument(validInt, intSchema))
    }

    @Test
    fun testArrayValidation() {
        val validArray = JExpression.JArray.fromSExpression(
            SExpression.List(listOf(SExpression.AtomInteger(1), SExpression.AtomInteger(2)))
        )
        kotlin.test.assertTrue(JSchema.validateDocument(validArray, arraySchema))
    }

    @Test
    fun testValidObjectValidation() {
        val validObject = JExpression.JObject.fromSExpression(
            SExpression.Array(
                listOf(
                    SExpression.AtomString("name"), SExpression.AtomString("Alice"),
                    SExpression.AtomString("age"), SExpression.AtomInteger(30)
                )
            )
        )
        kotlin.test.assertTrue(JSchema.validateDocument(validObject, objectSchema))
    }

    @Test
    fun testInvalidObjectValidation() {
        val invalidObject = JExpression.JObject.fromSExpression(
            SExpression.Array(listOf(SExpression.AtomString("name"), SExpression.AtomInteger(123)))
        )
        kotlin.test.assertFalse(JSchema.validateDocument(invalidObject, objectSchema))
    }

    private val nestedObjectSchema = JSchema.JObjectSchema(
        mapOf("city" to stringSchema, "zip" to intSchema),
        setOf("city")
    )

    private val complexObjectSchema = JSchema.JObjectSchema(
        mapOf(
            "name" to stringSchema,
            "age" to intSchema,
            "address" to nestedObjectSchema
        ),
        setOf("name", "address")
    )

    private val arrayOfObjectsSchema = JSchema.JArraySchema(complexObjectSchema)

    @Test
    fun testValidNestedObject() {
        val validObject = JExpression.JObject.fromSExpression(
            SExpression.Array(
                listOf(
                    SExpression.AtomString("city"), SExpression.AtomString("New York"),
                    SExpression.AtomString("zip"), SExpression.AtomInteger(10001)
                )
            )
        )
        kotlin.test.assertTrue(JSchema.validateDocument(validObject, nestedObjectSchema))
    }

    @Test
    fun testInvalidNestedObject() {
        val invalidObject = JExpression.JObject.fromSExpression(
            SExpression.Array(
                listOf(
                    SExpression.AtomString("city"), SExpression.AtomInteger(12345)
                )
            )
        )
        kotlin.test.assertFalse(JSchema.validateDocument(invalidObject, nestedObjectSchema))
    }

    @Test
    fun testValidComplexObject() {
        val validComplexObject = JExpression.JObject.fromSExpression(
            SExpression.Array(
                listOf(
                    SExpression.AtomString("name"), SExpression.AtomString("Alice"),
                    SExpression.AtomString("age"), SExpression.AtomInteger(30),
                    SExpression.AtomString("address"), SExpression.Array(
                        listOf(
                            SExpression.AtomString("city"), SExpression.AtomString("Los Angeles"),
                            SExpression.AtomString("zip"), SExpression.AtomInteger(90001)
                        )
                    )
                )
            )
        )

        println(validComplexObject)

        kotlin.test.assertTrue(JSchema.validateDocument(validComplexObject, complexObjectSchema))
    }

    @Test
    fun testInvalidComplexObject_MissingRequiredField() {
        val invalidComplexObject = JExpression.JObject.fromSExpression(
            SExpression.Array(
                listOf(
                    SExpression.AtomString("age"), SExpression.AtomInteger(25),
                    SExpression.AtomString("address"), SExpression.Array(
                        listOf(
                            SExpression.AtomString("city"), SExpression.AtomString("San Francisco")
                        )
                    )
                )
            )
        )
        kotlin.test.assertFalse(JSchema.validateDocument(invalidComplexObject, complexObjectSchema))
    }

    @Test
    fun testValidArrayOfObjects() {
        val validArray = JExpression.JArray.fromSExpression(
            SExpression.List(
                listOf(
                    SExpression.Array(
                        listOf(
                            SExpression.AtomString("name"), SExpression.AtomString("Bob"),
                            SExpression.AtomString("age"), SExpression.AtomInteger(40),
                            SExpression.AtomString("address"), SExpression.Array(
                                listOf(
                                    SExpression.AtomString("city"), SExpression.AtomString("Chicago"),
                                    SExpression.AtomString("zip"), SExpression.AtomInteger(60601)
                                )
                            )
                        )
                    ),
                    SExpression.Array(
                        listOf(
                            SExpression.AtomString("name"), SExpression.AtomString("Eve"),
                            SExpression.AtomString("age"), SExpression.AtomInteger(28),
                            SExpression.AtomString("address"), SExpression.Array(
                                listOf(
                                    SExpression.AtomString("city"), SExpression.AtomString("Seattle"),
                                    SExpression.AtomString("zip"), SExpression.AtomInteger(98101)
                                )
                            )
                        )
                    )
                )
            )
        )
        kotlin.test.assertTrue(JSchema.validateDocument(validArray, arrayOfObjectsSchema))
    }
}