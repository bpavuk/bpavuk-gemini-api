package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
data class GeminiRequestError(
    val code: Int,
    override val message: String,
    val status: String? = null
): Throwable(message = "$code: $status\n$message")

@Serializable
data class GeminiRequestErrorSurrogate(
    val error: GeminiRequestError
)
