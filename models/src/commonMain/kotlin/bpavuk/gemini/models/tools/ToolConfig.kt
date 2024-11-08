package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
public data class ToolConfig(
    val functionCallingConfig: FunctionCallingConfig? = null
)
