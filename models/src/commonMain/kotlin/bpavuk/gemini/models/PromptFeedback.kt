package bpavuk.gemini.models

import bpavuk.gemini.models.safety.BlockReason
import bpavuk.gemini.models.safety.SafetyRating
import kotlinx.serialization.Serializable

@Serializable
data class PromptFeedback(
    val blockReason: BlockReason,
    val safetyRatings: List<SafetyRating>
)