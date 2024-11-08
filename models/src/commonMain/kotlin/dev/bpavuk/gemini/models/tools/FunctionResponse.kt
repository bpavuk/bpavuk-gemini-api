package dev.bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
public data class FunctionResponse<T : ExpectedFunctionResult>(
    val name: String,
    val response: T
)
