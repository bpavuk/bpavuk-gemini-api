package dev.bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
public data class FileData(
    val mimeType: String,
    val fileUri: String
)
