package dev.bpavuk.gemini.models.safety

import kotlinx.serialization.Serializable

@Serializable
public data class SafetyRating(
    val category: HarmCategory,
    val probability: HarmProbability,
    val blocked: Boolean = false
)
