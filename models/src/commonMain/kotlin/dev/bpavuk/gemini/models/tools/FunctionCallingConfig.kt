package dev.bpavuk.gemini.models.tools

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class FunctionCallingConfig internal constructor(
    @SerialName("allowedFunctionNames")
    val allowedFunctionNamesString: List<String>,
    val mode: Mode
) {
    public constructor(
        mode: Mode,
        allowedFunctionNames: List<FunctionDeclaration>
    ) : this(
        mode = mode,
        allowedFunctionNamesString = allowedFunctionNames.map { it.name }
    )
}
