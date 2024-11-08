package dev.bpavuk.gemini.models

import dev.bpavuk.gemini.models.safety.SafetyRating
import kotlinx.serialization.Serializable

@Serializable
public data class Candidate(
    val content: Content? = null,
    val finishReason: FinishReason,
    val safetyRatings: List<SafetyRating>,
//    val citationMetadata: CitationMetadata,
    val tokenCount: Int? = null,
//    val groundingAttributions: List<GroundingAttribution>,
    val index: Int
)
