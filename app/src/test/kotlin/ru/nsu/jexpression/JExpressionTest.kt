import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import ru.nsu.jexpression.JExpression
import ru.nsu.sexpression.SExpression

class JExpressionTest {

    @Test
    fun `test JString from SExpression`() {
        val atomString = SExpression.AtomString("Hello")
        val jString = JExpression.JString.fromSExpression(atomString)

        assertEquals("Hello", jString.getValue())
        assertEquals(atomString.toString(), jString.toString())
    }

    @Test
    fun `test JInteger from SExpression`() {
        val atomInteger = SExpression.AtomInteger(42)
        val jInteger = JExpression.JInteger.fromSExpression(atomInteger)

        assertEquals(42, jInteger.getValue())
        assertEquals(atomInteger.toString(), jInteger.toString())
    }

    @Test
    fun `test JArray from SExpression`() {
        val listExpression = SExpression.List(
            listOf(
                SExpression.AtomString("item1"),
                SExpression.AtomInteger(1),
                SExpression.AtomString("item2"),
                SExpression.AtomInteger(2)
            )
        )
        val jArray = JExpression.JArray.fromSExpression(listExpression)

        val values = jArray.getValue()
        assertEquals(4, values.size)

        assertTrue(values[0] is JExpression.JString)
        assertTrue(values[1] is JExpression.JInteger)
        assertTrue(values[2] is JExpression.JString)
        assertTrue(values[3] is JExpression.JInteger)

        assertEquals("item1", (values[0] as JExpression.JString).getValue())
        assertEquals(1, (values[1] as JExpression.JInteger).getValue())
        assertEquals("item2", (values[2] as JExpression.JString).getValue())
        assertEquals(2, (values[3] as JExpression.JInteger).getValue())
    }

    @Test
    fun `test JObject from SExpression with valid format`() {
        val arrayExpression = SExpression.Array(
            listOf(
                SExpression.AtomString("key1"),
                SExpression.AtomInteger(10),
                SExpression.AtomString("key2"),
                SExpression.AtomString("value2")
            )
        )
        val jObject = JExpression.JObject.fromSExpression(arrayExpression)

        val value1 = (jObject.getValue()["key1"] as? JExpression.JInteger)?.getValue()
        val value2 = (jObject.getValue()["key2"] as? JExpression.JString)?.getValue()

        assertNotNull(value1)
        assertEquals(10, value1)
        assertNotNull(value2)
        assertEquals("value2", value2)
    }

    @Test
    fun `test JObject with invalid format (uneven number of elements)`() {
        val arrayExpression = SExpression.Array(
            listOf(
                SExpression.AtomString("key1"),
                SExpression.AtomInteger(10),
                SExpression.AtomString("key2")
            )
        )
        assertThrows(IllegalArgumentException::class.java) {
            JExpression.JObject.fromSExpression(arrayExpression)
        }
    }

    @Test
    fun `test JObject with duplicate keys`() {
        val arrayExpression = SExpression.Array(
            listOf(
                SExpression.AtomString("key1"),
                SExpression.AtomInteger(10),
                SExpression.AtomString("key1"),
                SExpression.AtomInteger(20)
            )
        )
        assertThrows(IllegalArgumentException::class.java) {
            JExpression.JObject.fromSExpression(arrayExpression)
        }
    }

    @Test
    fun `test fromSExpression with different types`() {
        val atomString = SExpression.AtomString("Test")
        val atomInteger = SExpression.AtomInteger(123)
        val listExpression = SExpression.List(listOf(atomString, atomInteger))
        val arrayExpression = SExpression.Array(
            listOf(
                SExpression.AtomString("key1"),
                SExpression.AtomInteger(10)
            )
        )

        val jString = JExpression.fromSExpression(atomString)
        val jInteger = JExpression.fromSExpression(atomInteger)
        val jArray = JExpression.fromSExpression(listExpression)
        val jObject = JExpression.fromSExpression(arrayExpression)

        assertTrue(jString is JExpression.JString)
        assertTrue(jInteger is JExpression.JInteger)
        assertTrue(jArray is JExpression.JArray)
        assertTrue(jObject is JExpression.JObject)
    }
}
