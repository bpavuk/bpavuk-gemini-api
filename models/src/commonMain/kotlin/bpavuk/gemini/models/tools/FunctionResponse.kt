package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
data class FunctionResponse<T>(
    val name: String,
    val response: T
)
