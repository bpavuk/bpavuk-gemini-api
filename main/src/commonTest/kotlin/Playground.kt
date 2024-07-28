import bpavuk.gemini.Gemini
import bpavuk.gemini.models.Content
import bpavuk.gemini.models.Part
import bpavuk.gemini.models.toSurrogate
import bpavuk.gemini.models.tools.*
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class Playground {

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
        val content = Content(role = "user", partSurrogates = listOf(Part.Text("hello").toSurrogate()))
        val response = gemini.generateContent(model, listOf(content))
        println(response)
    }

    @Test
    fun tools() = runTest {
        val smartHomeTool = Tool(
            functionDeclarations = listOf(
                FunctionDeclaration(
                    name = "toggleLightbulb",
                    parameters = Schema(type = Type.OBJECT, properties = mapOf("room" to Schema(type = Type.STRING))),
                    description = "Toggle lightbulb in a specified room."
                ),
                FunctionDeclaration(
                    name = "getRooms",
                    description = "Returns a list of available rooms."
                )
            )
        )

        val gemini = Gemini(apiKey = API_KEY)
        val model = gemini.getModels().first.first { it.name.contains("gemini-1.5-flash") }
        val content = Content(role = "user", parts = listOf(Part.Text("Give me a list of available rooms to control")))
        val response = gemini.generateContent(model, listOf(content), listOf(smartHomeTool))
        println(response)
    }
}
