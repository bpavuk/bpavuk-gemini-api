package bpavuk.gemini.models.surrogates

import bpavuk.gemini.models.Content
import bpavuk.gemini.models.safety.SafetySetting
import bpavuk.gemini.models.tools.Tool
import bpavuk.gemini.models.tools.ToolConfig
import kotlinx.serialization.Serializable

@Serializable
public data class GenerateContentSurrogate(
    val contents: List<Content>,
    val tools: List<Tool>? = null,
    val toolConfig: ToolConfig? = null,
    val safetySettings: List<SafetySetting>? = null,
//    val generationConfig: GenerationConfig? = null, TODO
    val systemInstruction: Content? = null
)
