package dev.bpavuk.gemini.models.surrogates

import dev.bpavuk.gemini.models.Content
import dev.bpavuk.gemini.models.safety.SafetySetting
import dev.bpavuk.gemini.models.tools.Tool
import dev.bpavuk.gemini.models.tools.ToolConfig
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
