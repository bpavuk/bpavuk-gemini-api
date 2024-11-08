package dev.bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
public data class GenerateContentResponse(
    val candidates: List<Candidate>? = null,
    val promptFeedback: PromptFeedback? = null,
    val usageMetadata: UsageMetadata? = null
)
