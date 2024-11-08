package bpavuk.gemini.models

import bpavuk.gemini.models.codeExec.CodeExecutionResult
import bpavuk.gemini.models.codeExec.ExecutableCode
import bpavuk.gemini.models.tools.ExpectedFunctionResult
import bpavuk.gemini.models.tools.FunctionCall
import bpavuk.gemini.models.tools.FunctionResponse
import kotlinx.serialization.Serializable


@Suppress("FunctionName")
public sealed interface Part {
    public data class Text(val text: String): Part
    public data class BlobSurrogate(val inlineData: Blob): Part
    public data class FunctionCallSurrogate<T : ExpectedFunctionResult>(val functionCall: FunctionCall<T>): Part
    public data class FunctionResponseSurrogate<T : ExpectedFunctionResult>(val functionResponse: FunctionResponse<T>): Part
    public data class FileDataSurrogate(val fileData: FileData): Part
    public data class ExecutableCodeSurrogate(val executableCode: ExecutableCode): Part
    public data class CodeExecutionResultSurrogate(val codeExecutionResult: CodeExecutionResult): Part

    public companion object {
        public fun Blob(mimeType: String, data: String): BlobSurrogate = BlobSurrogate(bpavuk.gemini.models.Blob(mimeType, data))

        public fun <T : ExpectedFunctionResult> FunctionCall(name: String, args: T): FunctionCallSurrogate<T> = FunctionCallSurrogate(
            bpavuk.gemini.models.tools.FunctionCall(name, args)
        )

        public fun <T : ExpectedFunctionResult> FunctionResponse(name: String, args: T): FunctionResponseSurrogate<T> = FunctionResponseSurrogate(
            bpavuk.gemini.models.tools.FunctionResponse(name, args)
        )

        public fun FileData(mimeType: String, fileUri: String): FileDataSurrogate = FileDataSurrogate(
            bpavuk.gemini.models.FileData(mimeType, fileUri)
        )

        public fun ExecutableCode(language: Language, code: String): ExecutableCodeSurrogate = ExecutableCodeSurrogate(
            bpavuk.gemini.models.codeExec.ExecutableCode(language, code)
        )
        public fun CodeExecutionResult(outcome: Outcome, result: String): CodeExecutionResultSurrogate = CodeExecutionResultSurrogate(
            bpavuk.gemini.models.codeExec.CodeExecutionResult(outcome, result)
        )
    }
}



@Suppress("UNCHECKED_CAST")
public fun <T : ExpectedFunctionResult> Part.toSurrogate(): PartSurrogate<T> {
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
public data class PartSurrogate<T : ExpectedFunctionResult>(
    val text: String? = null,
    val inlineData: Blob? = null,
    val functionCall: FunctionCall<T>? = null,
    val functionResponse: FunctionResponse<T>? = null,
    val fileData: FileData? = null,
    val executableCode: ExecutableCode? = null,
    val codeExecutionResult: CodeExecutionResult? = null
) {
    public fun toPart(): Part {
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
