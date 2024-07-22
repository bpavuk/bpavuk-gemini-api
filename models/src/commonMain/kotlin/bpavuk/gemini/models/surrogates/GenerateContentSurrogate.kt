package bpavuk.gemini.models.surrogates

import bpavuk.gemini.models.Content
import bpavuk.gemini.models.safety.SafetySetting
import kotlinx.serialization.Serializable

@Serializable
data class GenerateContentSurrogate(
    val contents: List<Content>,
//    val tools: List<Tool>? = null,  TODO
    val safetySettings: List<SafetySetting>? = null,
//    val generationConfig: GenerationConfig? = null, TODO
    val systemInstruction: Content? = null
)
