package bpavuk.gemini.models

import bpavuk.gemini.models.tools.FunctionCall
import bpavuk.gemini.models.tools.FunctionResponse
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable(Part.Serializer::class)
sealed interface Part {
    @Serializable
    data class Text(val text: String): Part

    @Serializable
    data class BlobSurrogate(val inlineData: Blob): Part

    @Serializable
    data class FunctionCallSurrogate<T>(val functionCall: FunctionCall<T>): Part

    @Serializable
    data class FunctionResponseSurrogate<T>(val functionResponse: FunctionResponse<T>): Part

    @Serializable
    data class FileDataSurrogate(val fileData: FileData): Part

    @Serializable
    data class ExecutableCodeSurrogate(val executableCode: ExecutableCode): Part

    @Serializable
    data class CodeExecutionResultSurrogate(val codeExecutionResult: CodeExecutionResult): Part
    
    object Serializer : KSerializer<Part> {
        @Serializable
        private data class Surrogate(
            val text: String? = null,
            val inlineData: Blob? = null,
            val functionCall: FunctionCall<@Contextual Any>? = null,
            val functionResponse: FunctionResponse<@Contextual Any>? = null,
            val fileData: FileData? = null,
            val executableCode: ExecutableCode? = null,
            val codeExecutionResult: CodeExecutionResult? = null
        )

        override val descriptor: SerialDescriptor = Surrogate.serializer().descriptor

        override fun serialize(encoder: Encoder, value: Part) {
            when (value) {
                is Text -> {
                    val surrogate = Surrogate(text = value.text)
                    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
                }
                is BlobSurrogate -> {
                    val surrogate = Surrogate(inlineData = value.inlineData)
                    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
                }

                is FunctionCallSurrogate<*> -> {
                    val surrogate = Surrogate(functionCall = value.functionCall as FunctionCall<Any>)
                    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
                }
                is FunctionResponseSurrogate<*> -> {
                    val surrogate = Surrogate(functionResponse = value.functionResponse as FunctionResponse<Any>)
                    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
                }

                is FileDataSurrogate -> {
                    val surrogate = Surrogate(fileData = value.fileData)
                    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
                }
                is ExecutableCodeSurrogate -> {
                    val surrogate = Surrogate(executableCode = value.executableCode)
                    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
                }
                is CodeExecutionResultSurrogate -> {
                    val surrogate = Surrogate(codeExecutionResult = value.codeExecutionResult)
                    encoder.encodeSerializableValue(Surrogate.serializer(), surrogate)
                }
            }
        }

        override fun deserialize(decoder: Decoder): Part {
            val data = decoder.decodeSerializableValue(Surrogate.serializer())
            return when {
                data.text != null -> Text(data.text)
                data.inlineData != null -> BlobSurrogate(data.inlineData)
                data.functionCall != null -> FunctionCallSurrogate(data.functionCall)
                data.functionResponse != null -> FunctionResponseSurrogate(data.functionResponse)
                data.fileData != null -> FileDataSurrogate(data.fileData)
                data.executableCode != null -> ExecutableCodeSurrogate(data.executableCode)
                data.codeExecutionResult != null -> CodeExecutionResultSurrogate(data.codeExecutionResult)
                else -> error("Unknown part data")
            }
        }
    }
}
