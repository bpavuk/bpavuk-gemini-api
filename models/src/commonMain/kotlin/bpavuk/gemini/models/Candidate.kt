package bpavuk.gemini.models

import bpavuk.gemini.models.safety.SafetyRating
import kotlinx.serialization.Serializable

@Serializable
data class Candidate(
    val content: Content,
    val finishReason: FinishReason,
    val safetyRatings: List<SafetyRating>,
//    val citationMetadata: CitationMetadata,
    val tokenCount: Int? = null,
//    val groundingAttributions: List<GroundingAttribution>,
    val index: Int
)
