package dev.bpavuk.gemini

import dev.bpavuk.gemini.models.Content
import dev.bpavuk.gemini.models.Language
import dev.bpavuk.gemini.models.Outcome
import dev.bpavuk.gemini.models.Part
import dev.bpavuk.gemini.models.tools.ExpectedFunctionResult

public interface ContentBuilder {
    public val role: String
    public fun part(part: Part)
    public fun text(text: String): Unit = part(Part.Text(text))
    public fun blob(mimeType: String, data: String): Unit = part(Part.Blob(mimeType, data))
    public fun codeExecutionResult(outcome: Outcome, result: String): Unit =
        part(Part.CodeExecutionResult(outcome, result))
    public fun <T : ExpectedFunctionResult> functionResponse(name: String, args: T): Unit =
        part(Part.FunctionResponse(name, args))
    public fun <T : ExpectedFunctionResult> functionCall(name: String, args: T): Unit =
        part(Part.FunctionCall(name, args))
    public fun code(language: Language, code: String): Unit = part(Part.ExecutableCode(language, code))
    public fun fileData(mimeType: String, fileUri: String): Unit = part(Part.FileData(mimeType, fileUri))
}

private class ContentBuilderImpl(override val role: String) : ContentBuilder {
    val partsList: MutableList<Part> = mutableListOf()
    override fun part(part: Part) {
        partsList.add(part)
    }

    fun build(): Content = Content(parts = partsList, role = role)
}

public fun content(role: String, contentBuilder: ContentBuilder.() -> Unit): Content =
    ContentBuilderImpl(role).apply(contentBuilder).build()
