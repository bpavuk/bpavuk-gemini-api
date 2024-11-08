package dev.bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
public data class UsageMetadata(
    val promptTokenCount: Int? = null,
    val cachedContentTokenCount: Int? = null,
    val candidatesTokenCount: Int? = null,
    val totalTokenCount: Int
)