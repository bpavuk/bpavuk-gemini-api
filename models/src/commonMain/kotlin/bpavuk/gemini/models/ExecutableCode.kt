package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
data class ExecutableCode(
    val language: Language,
    val code: String
)
