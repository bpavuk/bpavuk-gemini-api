import bpavuk.gemini.Gemini
import bpavuk.gemini.models.Content
import bpavuk.gemini.models.tools.FunctionCall
import bpavuk.gemini.models.Part
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test

class Playground {
    val json = Json { prettyPrint = true }

    @Test
    fun test() = runTest {
        val part = Part.Text("fuckery")
        println(part)
        val jsonString = json.encodeToString(part)
        println(jsonString)

        val jsonString2 = """
           {
               "text": "fuckery"
           } 
        """.trimIndent()
        val part2 = Json.decodeFromString<Part>(jsonString2)
        println(part2)

        // function calls
        val fnCall = Part.FunctionCallSurrogate(FunctionCall("hello", listOf("world")))
        println(fnCall)
        val jsonString3 = json.encodeToString(fnCall)
        println(jsonString3)

        val fnCallString = """
        {
            "functionCall": {
                "name": "hello",
                "args": [
                    "world"
                ]
            }
        } 
        """.trimIndent()

        val fnCall2 = Json.decodeFromString<Part>(fnCallString)
        println(fnCall2)
    }

    @Test
    fun getModelsList() = runTest {
        val gemini = Gemini(apiKey = API_KEY)
        val models = gemini.getModels()
        println(models)
    }

    @Test
    fun generateContent() = runTest {
        val gemini = Gemini(apiKey = API_KEY)
        val model = gemini.getModels().first.first { it.name.contains("gemini-1.5-flash") }
        val content = Content(parts = listOf(Part.Text("hello")), role = "user")
        val response = gemini.generateContent(model, listOf(content))
        println(response)
    }

    @Test
    fun streamGenerateContent() = runTest {

    }
}
