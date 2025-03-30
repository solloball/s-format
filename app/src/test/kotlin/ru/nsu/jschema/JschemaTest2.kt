package ru.nsu.jschema

import org.junit.jupiter.api.Test
import ru.nsu.jexpression.JExpression
import ru.nsu.sexpression.SExpression
import ru.nsu.sexpression.SExpressionParserDumb
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class PetStoreSchemaTest {
    private val stringSchema = JSchema.JStringSchema
    private val intSchema = JSchema.JIntegerSchema
    private val parser = SExpressionParserDumb()

    private val productSchema = JSchema.JObjectSchema(
        mapOf(
            "name" to stringSchema,
            "price" to intSchema,
            "category" to stringSchema
        ),
        setOf("name", "price")
    )

    private val storeSchema = JSchema.JObjectSchema(
        mapOf(
            "storeName" to stringSchema,
            "location" to stringSchema,
            "products" to JSchema.JArraySchema(productSchema)
        ),
        setOf("storeName", "products")
    )

    @Test
    fun testValidPetStore() {
        val validPetStore = JExpression.JObject.fromSExpression(
            SExpression.Array(
                listOf(
                    SExpression.AtomString("storeName"), SExpression.AtomString("Happy Pets"),
                    SExpression.AtomString("location"), SExpression.AtomString("Main Street"),
                    SExpression.AtomString("products"), SExpression.List(
                        listOf(
                            SExpression.Array(
                                listOf(
                                    SExpression.AtomString("name"), SExpression.AtomString("Dog Food"),
                                    SExpression.AtomString("price"), SExpression.AtomInteger(20),
                                    SExpression.AtomString("category"), SExpression.AtomString("Food")
                                )
                            ),
                            SExpression.Array(
                                listOf(
                                    SExpression.AtomString("name"), SExpression.AtomString("Cat Toy"),
                                    SExpression.AtomString("price"), SExpression.AtomInteger(10)
                                )
                            ),
                            SExpression.Array(
                                listOf(
                                    SExpression.AtomString("name"), SExpression.AtomString("Cat Toy"),
                                    SExpression.AtomString("price"), SExpression.AtomInteger(10)
                                )
                            )
                        )
                    )
                )
            )
        )
        assertTrue(JSchema.validateDocument(validPetStore, storeSchema))
    }

    @Test
    fun testInvalidPetStore_MissingRequiredField() {
        val invalidPetStore = JExpression.JObject.fromSExpression(
            SExpression.Array(
                listOf(
                    SExpression.AtomString("location"), SExpression.AtomString("Downtown"),
                    SExpression.AtomString("products"), SExpression.List(
                        listOf(
                            SExpression.Array(
                                listOf(
                                    SExpression.AtomString("name"), SExpression.AtomString("Bird Cage"),
                                    SExpression.AtomString("price"), SExpression.AtomInteger(50)
                                )
                            )
                        )
                    )
                )
            )
        )
        assertFalse(JSchema.validateDocument(invalidPetStore, storeSchema))
    }

    @Test
    fun testInvalidPetStoreMissingRequiredField() {
        val invalidPetStore = JExpression.fromSExpression(parser.parse(
            "[ location 'Downtown' products ( [ name 'Bird Cage' price 50 ] ) ]"
        ))
        assertFalse(JSchema.validateDocument(invalidPetStore, storeSchema))
    }

    @Test
    fun testValidPetStoreMultipleProducts() {
        val validPetStore = JExpression.fromSExpression(parser.parse(
            "[ storeName \"Best Pets\" location \"Main Street\" products ( " +
                    "[ name \"Dog Food\" price 25 category \"Food\" ] " +
                    "[ name \"Cat Litter\" price 15 category \"Hygiene\" ] " +
                    "[ name \"Bird Cage\" price 50 category \"Housing\" ] " +
                    ") ]"
        ))
        assertTrue(JSchema.validateDocument(validPetStore, storeSchema))
    }
}
