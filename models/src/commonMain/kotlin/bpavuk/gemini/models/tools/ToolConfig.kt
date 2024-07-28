package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
data class ToolConfig(
    val functionCallingConfig: FunctionCallingConfig? = null
)
