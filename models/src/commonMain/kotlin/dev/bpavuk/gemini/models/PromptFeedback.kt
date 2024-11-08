package dev.bpavuk.gemini.models

import dev.bpavuk.gemini.models.safety.BlockReason
import dev.bpavuk.gemini.models.safety.SafetyRating
import kotlinx.serialization.Serializable

@Serializable
public data class PromptFeedback(
    val blockReason: BlockReason,
    val safetyRatings: List<SafetyRating>
)