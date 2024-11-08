package dev.bpavuk.gemini.models

import dev.bpavuk.gemini.models.tools.ExpectedFunctionResult
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class Content(
    val role: String,
    @SerialName("parts")
    val partSurrogates: List<PartSurrogate<@Polymorphic ExpectedFunctionResult>>
) {
    public constructor(parts: List<Part>, role: String) : this(role, parts.map { it.toSurrogate() })
}
