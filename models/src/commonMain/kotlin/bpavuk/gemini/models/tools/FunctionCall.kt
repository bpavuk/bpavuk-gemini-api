package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
data class FunctionCall<T>(
    val name: String,
    val args: T? = null
)
