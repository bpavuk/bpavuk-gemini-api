package bpavuk.gemini.models.safety

import kotlinx.serialization.Serializable

@Serializable
data class SafetyRating(
    val category: HarmCategory,
    val probability: HarmProbability,
    val blocked: Boolean = false
)
