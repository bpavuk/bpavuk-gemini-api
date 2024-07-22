package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
data class Model(
    val name: String,
    val baseModelId: String? = null,
    val version: String,
    val displayName: String? = null,
    val description: String? = null,
    val inputTokenLimit: Int? = null,
    val outputTokenLimit: Int? = null,
    val supportedGenerationMethods: List<String>? = null,
    val temperature: Double? = null,
    val maxTemperature: Double? = null,
    val topP: Double? = null,
    val topK: Int? = null
)
