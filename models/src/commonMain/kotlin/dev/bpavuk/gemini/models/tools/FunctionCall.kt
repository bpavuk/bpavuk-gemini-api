package dev.bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
public data class FunctionCall<T>(
    val name: String,
    val args: T? = null
)
