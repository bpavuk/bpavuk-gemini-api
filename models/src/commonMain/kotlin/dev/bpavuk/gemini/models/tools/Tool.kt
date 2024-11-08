package dev.bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
public data class Tool(
    val functionDeclarations: List<FunctionDeclaration>
)
