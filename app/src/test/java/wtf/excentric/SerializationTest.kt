package wtf.excentric

import kotlinx.serialization.builtins.list
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import wtf.excentric.feature.search.CatImage


class SerializationTest {

    @Test
    fun testSerialization() {
        val jsonString = """[{"id": "123", "url": "https://google.com"}]"""
        val result = Json.parse(CatImage.serializer().list, jsonString)
        assertEquals(listOf(CatImage(id = "123", url = "https://google.com")), result)
    }

}