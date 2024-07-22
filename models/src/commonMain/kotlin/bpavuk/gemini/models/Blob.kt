package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
data class Blob(
    val mimeType: String,
    val data: String
)
