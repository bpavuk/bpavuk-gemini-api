package bpavuk.gemini.examples

import bpavuk.gemini.Gemini
import bpavuk.gemini.models.Content
import bpavuk.gemini.models.Part

suspend fun main() {
    println("Initializing...")
    val history = mutableListOf<Content>()
    var outputStats = false


    println("Enter your Gemini API key:")
    val API_KEY = readln()
    val gemini = Gemini(apiKey = API_KEY)

    val models = gemini.getModels()
    var model = models.first.first { it.name.contains("gemini-1.5-flash") }

    fun changeModel() {
        println("Available models:")
        for (i in models.first.indices) {
            println("${i + 1}. ${models.first[i].displayName ?: models.first[i].name}")
        }
        print("Type the model number: ")
        val modelIndex = readln().toInt()
        model = models.first[modelIndex - 1]
        println("Model: ${model.displayName ?: model.name}")
    }

    suspend fun sendMessage(message: String, outputStats: Boolean = false) {
        val content = Content(parts = listOf(Part.Text(message)), role = "user")
        val response = gemini.generateContent(model, history + content)
        val responseCandidate = response.candidates.firstOrNull()
        if (responseCandidate != null) {
            history.add(content)
            val responseText = responseCandidate.content.parts.first() as? Part.Text
            if (responseText != null) println(responseText.text)
            else println("Any other responses but text are not supported yet")
        }
        if (outputStats) println(
            "STATS - ${
                response.usageMetadata?.let {
                    "token count: ${it.totalTokenCount}, "
                }
            }"
            + "${
                responseCandidate?.finishReason?.let { "finish reason: ${it.name}" }
            }"
        )
    }

    println("Default model: Gemini 1.5 Flash. Wanna change? [y/n]")
    if (readlnOrNull() == "y") {
        changeModel()
    }

    println("Ready. To exit, type /goodbye")
    while (true) {
        print(">>> ")
        val input = readlnOrNull() ?: ""
        when {
            input == "/model" -> changeModel()
            input == "/goodbye" -> break
            input == "/toggleStats" -> outputStats = !outputStats
            input.isEmpty() -> continue
            else -> {
                sendMessage(input, outputStats)
            }
        }
    }
    println("Bye!")
}
