package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val mimeType: String,
    val fileUri: String
)
