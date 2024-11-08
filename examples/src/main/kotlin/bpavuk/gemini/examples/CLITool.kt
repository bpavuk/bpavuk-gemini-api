package bpavuk.gemini.examples

import bpavuk.gemini.models.tools.ExpectedFunctionResult
import bpavuk.gemini.models.tools.Schema
import bpavuk.gemini.models.tools.Type
import bpavuk.gemini.tools.tool
import kotlinx.serialization.Serializable
import java.io.IOException

val CLITool = tool {
    function(
        name = "execute",
        parameters = Schema(
            type = Type.OBJECT,
            properties = mapOf(
                "command" to Schema(type = Type.STRING, nullable = false, description = "Shell command to execute."),
                "type" to Schema(
                    type = Type.STRING,
                    nullable = false,
                    description = "Type discriminator. Should always be 'bpavuk.gemini.examples.Command'."
                ),
                "wait" to Schema(
                    type = Type.BOOLEAN,
                    nullable = false,
                    description = "Wait for command to finish. " +
                            "Leave it false if the execution will hang up the whole system."
                )
            ),
            nullable = false,
            required = listOf("command", "type", "wait")
        ),
        description = "Executes a shell command. This allows you to execute any command on user's PC. " +
                "They'll see a consent request window after you return an execution request" +
                " and give a comment on why they refused."
    )
    function(
        name = "outlineExecutionPlan",
        parameters = Schema(
            type = Type.OBJECT,
            properties = mapOf(
                "goal" to Schema(type = Type.STRING, nullable = false, description = "Goal to achieve."),
                "plan" to Schema(type = Type.STRING, nullable = false, description = "Execution plan to outline."),
                "type" to Schema(
                    type = Type.STRING,
                    nullable = false,
                    description = "Type discriminator. Should always be 'bpavuk.gemini.examples.ExecutionPlan'."
                )
            ),
            required = listOf("goal", "plan", "type")
        ),
        description = "You can use this function to outline your plan of achieving the set goal. " +
                "It will respond nothing. Use it as a place for drafting. User won't see that."
    )
    function(
        name = "comment",
        description = "if you plan to generate something afterwards and just want " +
                "to give the visible-to-user comment on current situation. " +
                "you can use this to take multiple turns of generations. DON'T GENERATE USUAL 'text' UNTIL YOU'RE DONE.",
        parameters = Schema(
            type = Type.OBJECT,
            properties = mapOf(
                "comment" to Schema(type = Type.STRING, nullable = false, description = "Comment to be added."),
                "type" to Schema(
                    type = Type.STRING,
                    nullable = false,
                    description = "Type discriminator. Should always be 'bpavuk.gemini.examples.Comment'."
                )
            ),
            required = listOf("comment", "type")
        )
    )
}

@Serializable
data class Command(
    val command: String,
    val wait: Boolean,
) : ExpectedFunctionResult

@Serializable
sealed interface CommandExecutionResult : ExpectedFunctionResult {
    @Serializable
    data class Consent(val output: String) : CommandExecutionResult

    @Serializable
    data class ConsentWasNotGiven(val reason: String? = null) : CommandExecutionResult
}

@Serializable
data class Comment(
    val comment: String
) : ExpectedFunctionResult

@Serializable
data class ExecutionPlan(
    val goal: String,
    val plan: String
) : ExpectedFunctionResult

fun executeCommand(command: String, wait: Boolean = true): String {
    try {
        val process = ProcessBuilder(*command.split("\\s".toRegex()).toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()

        var output = ""
        var buffer = ""

        if (wait) {
            process.waitFor()
            buffer += process.inputStream.bufferedReader().readText()
            output += buffer
            println(buffer)
        }

        return if (wait) {
            """
            command: $command
            -------------------
            stdout: $buffer
            -------------------
            stderr: ${process.errorStream.bufferedReader().readText()}
            """.trimIndent()
        } else {
            """
            command: $command
            leaving process running with pid ${process.pid()}. logs are inaccessible.
            """.trimIndent()
        }
    } catch (e: IOException) {
        return "Runtime exception. ${e::class} : ${e.message}"
    }
}
