package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
data class GenerateContentResponse(
    val candidates: List<Candidate>,
    val promptFeedback: PromptFeedback? = null,
    val usageMetadata: UsageMetadata? = null
)
