package bpavuk.gemini.models.codeExec

import bpavuk.gemini.models.Language
import kotlinx.serialization.Serializable

@Serializable
public data class ExecutableCode(
    val language: Language,
    val code: String
)
