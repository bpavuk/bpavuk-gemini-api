package bpavuk.gemini.models.safety

import kotlinx.serialization.Serializable

@Serializable
data class SafetySetting(
    val category: HarmCategory,
    val threshold: HarmBlockThreshold
)
