package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
public data class GeminiRequestError(
    val code: Int,
    override val message: String,
    val status: String? = null
): Throwable(message = "$code: $status\n$message")

@Serializable
public data class GeminiRequestErrorSurrogate(
    val error: GeminiRequestError
)
