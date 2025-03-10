package ru.nsu.jschema

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import ru.nsu.jexpression.JExpression
import ru.nsu.sexpression.SExpressionParserDumb

class JSchemaTest {

    private val parser = SExpressionParserDumb()

    @Test
    fun `test valid object`() {
        val schema = JSchema.ObjectType(
            mapOf(
                "name" to JSchema.StringType,
                "age" to JSchema.IntegerType
            )
        )

        val validExpr = JExpression.fromSExpression(parser.parse("[ Person name \"John\" age 30 ]"))

        assertTrue(schema.validate(validExpr))
    }

    @Test
    fun `test invalid object - missing field`() {
        val schema = JSchema.ObjectType(
            mapOf(
                "name" to JSchema.StringType,
                "age" to JSchema.IntegerType
            )
        )

        val invalidExpr = JExpression.fromSExpression(parser.parse("[ Person name \"John\" ]"))

        assertFalse(schema.validate(invalidExpr))
    }

    @Test
    fun `test invalid object - wrong type`() {
        val schema = JSchema.ObjectType(
            mapOf(
                "name" to JSchema.StringType,
                "age" to JSchema.IntegerType
            )
        )

        val invalidExpr = JExpression.fromSExpression(parser.parse("[ Person name 123 age 30 ]")) // Ошибка: name должен быть строкой

        assertFalse(schema.validate(invalidExpr))
    }

    @Test
    fun `test valid array`() {
        val schema = JSchema.ArrayType(JSchema.IntegerType)

        val validArray = JExpression.fromSExpression(parser.parse("( 1 2 3 )"))

        assertTrue(schema.validate(validArray))
    }

    @Test
    fun `test invalid array - mixed types`() {
        val schema = JSchema.ArrayType(JSchema.IntegerType)

        val invalidArray = JExpression.fromSExpression(parser.parse("( 1 \"two\" 3 )"))

        assertFalse(schema.validate(invalidArray))
    }

    @Test
    fun `test object with extra fields allowed`() {
        val schema = JSchema.ObjectType(
            mapOf(
                "name" to JSchema.StringType,
                "age" to JSchema.IntegerType
            ),
            allowExtraFields = true // Разрешаем дополнительные поля
        )

        val extraFieldExpr = JExpression.fromSExpression(parser.parse("[ Person name \"John\" age 30 extra \"data\" ]"))

        assertTrue(schema.validate(extraFieldExpr))
    }

    @Test
    fun `test object with extra fields not allowed`() {
        val schema = JSchema.ObjectType(
            mapOf(
                "name" to JSchema.StringType,
                "age" to JSchema.IntegerType
            ),
            allowExtraFields = false // Запрещаем лишние поля
        )

        val extraFieldExpr = JExpression.fromSExpression(parser.parse("[ Person name \"John\" age 30 extra \"data\" ]"))

        assertFalse(schema.validate(extraFieldExpr))
    }

    @Test
    fun `test object with extra fields`() {
        val schema = JSchema.ObjectType(
            mapOf(
                "name" to JSchema.StringType,
                "age" to JSchema.IntegerType,
                "surname" to JSchema.StringType,
            ),
            allowExtraFields = false
        )

        val extraFieldExpr = JExpression.fromSExpression(parser.parse("[ Person surname \"Watson\" name \"John\" age 30 extra \"data\" ]"))

        assertFalse(schema.validate(extraFieldExpr))
    }
}