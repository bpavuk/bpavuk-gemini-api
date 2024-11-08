package bpavuk.gemini.models.safety

import kotlinx.serialization.Serializable

@Serializable
public data class SafetySetting(
    val category: HarmCategory,
    val threshold: HarmBlockThreshold
)
