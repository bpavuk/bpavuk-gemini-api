package bpavuk.gemini.models

import bpavuk.gemini.models.codeExec.CodeExecutionResult
import bpavuk.gemini.models.codeExec.ExecutableCode
import bpavuk.gemini.models.tools.ExpectedFunctionResult
import bpavuk.gemini.models.tools.FunctionCall
import bpavuk.gemini.models.tools.FunctionResponse
import kotlinx.serialization.Serializable


@Suppress("FunctionName")
sealed interface Part {
    data class Text(val text: String): Part
    data class BlobSurrogate(val inlineData: Blob): Part
    data class FunctionCallSurrogate<T : ExpectedFunctionResult>(val functionCall: FunctionCall<T>): Part
    data class FunctionResponseSurrogate<T : ExpectedFunctionResult>(val functionResponse: FunctionResponse<T>): Part
    data class FileDataSurrogate(val fileData: FileData): Part
    data class ExecutableCodeSurrogate(val executableCode: ExecutableCode): Part
    data class CodeExecutionResultSurrogate(val codeExecutionResult: CodeExecutionResult): Part

    companion object {
        fun Blob(mimeType: String, data: String) = BlobSurrogate(bpavuk.gemini.models.Blob(mimeType, data))

        fun <T : ExpectedFunctionResult> FunctionCall(name: String, args: T) = FunctionCallSurrogate(
            bpavuk.gemini.models.tools.FunctionCall(name, args)
        )

        fun <T : ExpectedFunctionResult> FunctionResponse(name: String, args: T) = FunctionResponseSurrogate(
            bpavuk.gemini.models.tools.FunctionResponse(name, args)
        )

        fun FileData(mimeType: String, fileUri: String) = FileDataSurrogate(
            bpavuk.gemini.models.FileData(mimeType, fileUri)
        )

        fun ExecutableCode(language: Language, code: String) = ExecutableCodeSurrogate(
            bpavuk.gemini.models.codeExec.ExecutableCode(language, code)
        )
        fun CodeExecutionResult(outcome: Outcome, result: String) = CodeExecutionResultSurrogate(
            bpavuk.gemini.models.codeExec.CodeExecutionResult(outcome, result)
        )
    }
}



@Suppress("UNCHECKED_CAST")
fun <T : ExpectedFunctionResult> Part.toSurrogate(): PartSurrogate<T> {
    return when (this) {
        is Part.Text -> PartSurrogate(text = text)
        is Part.BlobSurrogate -> PartSurrogate(inlineData = inlineData)
        is Part.FunctionCallSurrogate<*> -> PartSurrogate(functionCall = functionCall)
                as? PartSurrogate<T> ?: error("Function call type mismatches requested")
        is Part.FunctionResponseSurrogate<*> -> PartSurrogate(functionResponse = functionResponse)
                as? PartSurrogate<T> ?: error("Function response type mismatches requested")
        is Part.FileDataSurrogate -> PartSurrogate(fileData = fileData)
        is Part.ExecutableCodeSurrogate -> PartSurrogate(executableCode = executableCode)
        is Part.CodeExecutionResultSurrogate -> PartSurrogate(codeExecutionResult = codeExecutionResult)
    }
}

@Serializable
data class PartSurrogate<T : ExpectedFunctionResult>(
    val text: String? = null,
    val inlineData: Blob? = null,
    val functionCall: FunctionCall<T>? = null,
    val functionResponse: FunctionResponse<T>? = null,
    val fileData: FileData? = null,
    val executableCode: ExecutableCode? = null,
    val codeExecutionResult: CodeExecutionResult? = null
) {
    fun toPart(): Part {
        return when {
            text != null -> Part.Text(text)
            inlineData != null -> Part.BlobSurrogate(inlineData)
            functionCall != null -> Part.FunctionCallSurrogate(functionCall)
            functionResponse != null -> Part.FunctionResponseSurrogate(functionResponse)
            fileData != null -> Part.FileDataSurrogate(fileData)
            executableCode != null -> Part.ExecutableCodeSurrogate(executableCode)
            codeExecutionResult != null -> Part.CodeExecutionResultSurrogate(codeExecutionResult)
            else -> error("Unknown part type")
        }
    }
}
