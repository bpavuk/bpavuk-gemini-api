package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
data class CodeExecutionResult(
    val outcome: Outcome,
    val output: String
)
