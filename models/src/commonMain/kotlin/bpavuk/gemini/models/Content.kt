package bpavuk.gemini.models

import bpavuk.gemini.models.tools.ExpectedFunctionResult
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val role: String,
    @SerialName("parts")
    val partSurrogates: List<PartSurrogate<@Polymorphic ExpectedFunctionResult>>
) {
    constructor(parts: List<Part>, role: String) : this(role, parts.map { it.toSurrogate() })
}
