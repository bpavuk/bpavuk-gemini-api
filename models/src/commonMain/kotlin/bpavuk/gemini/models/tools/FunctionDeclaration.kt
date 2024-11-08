package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
public data class FunctionDeclaration(
    val name: String,
    val description: String,
    val parameters: Schema? = null
)
