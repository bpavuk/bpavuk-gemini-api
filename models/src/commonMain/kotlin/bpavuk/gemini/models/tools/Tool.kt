package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
data class Tool(
    val functionDeclarations: List<FunctionDeclaration>
)
