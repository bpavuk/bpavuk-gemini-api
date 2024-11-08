package dev.bpavuk.gemini.models.codeExec

import dev.bpavuk.gemini.models.Outcome
import kotlinx.serialization.Serializable

@Serializable
public data class CodeExecutionResult(
    val outcome: Outcome,
    val output: String
)
