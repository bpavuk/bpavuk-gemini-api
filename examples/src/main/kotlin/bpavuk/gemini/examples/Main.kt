package bpavuk.gemini.examples

import bpavuk.gemini.Gemini
import bpavuk.gemini.content
import bpavuk.gemini.models.Content
import bpavuk.gemini.models.Model
import bpavuk.gemini.models.Part
import bpavuk.gemini.models.safety.HarmBlockThreshold
import bpavuk.gemini.models.safety.HarmCategory
import bpavuk.gemini.models.safety.SafetySetting
import bpavuk.gemini.models.tools.ExpectedFunctionResult
import bpavuk.gemini.models.tools.FunctionCall
import bpavuk.gemini.safetySettings
import kotlinx.serialization.modules.subclass

lateinit var model: Model
val history = mutableListOf<Content>()
lateinit var gemini: Gemini
var outputStats = false
val tools = mutableListOf(CLITool)
val safetySettings = safetySettings {
    dangerousContent = HarmBlockThreshold.BLOCK_NONE
    sexuallyExplicit = HarmBlockThreshold.BLOCK_NONE
}
val systemInstruction = "You can execute any command on user's hardware. " +
        "They'll see a consent request window after you send an execution request, so keep commands as simple as possible. " +
        "User always knows if command is harmful or not. They will stop you by themself if you try to do something harmful. " +
        "Just to be precautious, use --help flag extensively to learn about the tool" +
        " in context, even if it is well-known tool like the ones from coreutils. " +
        "This will allow you not only to learn about new tools but refresh the memory of newer ones or adapt to older versions.\n" +
        "You can also plan and draft your steps. Use your step-by-step thinking skills to your advantage.\n" +
        "Prefer to encase the paths in 'single quotation marks'\n" +
        "You can access files, download them, browse the internet (if you somehow manage to do that via CLI), everything!"

suspend fun main() {
    println("Initializing...")
    print("Enter your Gemini API key (or generate one at https:://aistudio.google.com/): ")
    val API_KEY = readln()

    gemini = Gemini(
        apiKey = API_KEY,
        argumentSerializers = {
            subclass(Command::class)
            subclass(CommandExecutionResult.ConsentWasNotGiven::class)
            subclass(CommandExecutionResult.Consent::class)
            subclass(ExecutionPlan::class)
            subclass(Comment::class)
        }
    )

    val models = gemini.getModels()

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
        val content = content(role = "user") { text(message) }
        history.add(content)
        val response = gemini.generateContent(
            model,
            history,
            tools.toList(),
            safetySettings,
            systemInstruction
        )
        val responseCandidate = response.candidates?.firstOrNull()
        if (responseCandidate != null) {
            val responseContent = responseCandidate.content
            if (responseContent != null) processResponse(responseContent, outputStats)
            else println("[ERROR] No content. Check the finish reason.")
        }
        if (outputStats) println(
            "STATS - ${
                response.usageMetadata?.let {
                    "token count: ${it.totalTokenCount}, "
                }
            }"
            + "${
                responseCandidate?.finishReason?.let { "finish reason: ${it.name}, " }
            }"
            + "${
                responseCandidate?.finishReason?.let { 
                    "safety rating: ${responseCandidate.safetyRatings}"
                }
            }"
        )
    }

    changeModel()

    println("Ready. To exit, type /goodbye")
    while (true) {
        print(">>> ")
        val input = readlnOrNull() ?: ""
        when {
            input == "/model" -> changeModel()
            input == "/goodbye" -> break
            input == "/toggleStats" -> outputStats = !outputStats
            input == "/history" -> println(history)
            input.startsWith("/exec ") -> executeCommand(input.removePrefix("/exec "))
            input.isEmpty() -> continue
            else -> {
                sendMessage(input, outputStats)
            }
        }
    }
    println("Bye!")
}

suspend fun processResponse(response: Content, outputStats: Boolean) {
    if (outputStats) println(response.partSurrogates)
    history.add(response)
    val parts = response.partSurrogates.map { it.toPart() }
    for (part in parts) {
        when (part) {
            is Part.FunctionResponseSurrogate<*> -> {
                println("You should not see this line")
            }
            is Part.FunctionCallSurrogate<*> -> {
                processFunctionCall(
                    part.functionCall,
                    part == (parts.filter {
                        it is Part.FunctionCallSurrogate<*> || it is Part.FunctionResponseSurrogate<*>
                    }).lastOrNull()
                )
            }
            is Part.Text -> {
                processText(part)
            }
            else -> {
                println("Any other responses but text are not supported yet.")
                if (outputStats) println(part)
            }
        }
    }
}

suspend fun processFunctionCall(functionCall: FunctionCall<out ExpectedFunctionResult>, generateContentAfterwards: Boolean = true) {
    when (functionCall.name) {
        "execute" -> {
            val command = functionCall.args as Command
            println(
                """
                [TOOLS] Gemini requested your consent to execute a command on your PC. Command:
                ${command.command}
                ${if (!command.wait) "Without waiting for a response (won't hang up the chat, logs won't be accessible)"
                else "With waiting for a response (may hang up the chat, logs will be accessible)"}
                """.trimIndent()
            )
            print("[y/n/refuse reason]    ")
            val functionResponse = content(role = "api") {
                functionResponse(
                    "execute",
                    when (val input = readln()) {
                        "y" -> {
                            val output = executeCommand(command.command, command.wait)
                            CommandExecutionResult.Consent(output)
                        }
                        "n" -> CommandExecutionResult.ConsentWasNotGiven()
                        else -> CommandExecutionResult.ConsentWasNotGiven(reason = input)
                    }
                )
            }
            history.add(functionResponse)
        }
        "comment" -> {
            val comment = functionCall.args as Comment
            println(comment.comment)
        }
    }
    if (generateContentAfterwards) {
        val response = gemini.generateContent(
            model,
            history,
            tools.toList(),
            safetySettings,
            systemInstruction
        ).candidates?.first()?.content
        if (response != null) processResponse(response, outputStats)
    }
}

fun processText(text: Part.Text) {
    println(text.text)
}
