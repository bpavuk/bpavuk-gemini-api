package dev.bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
public data class Blob(
    val mimeType: String,
    val data: String
)
