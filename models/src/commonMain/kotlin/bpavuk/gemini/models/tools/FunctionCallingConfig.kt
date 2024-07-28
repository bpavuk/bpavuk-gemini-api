package bpavuk.gemini.models.tools

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FunctionCallingConfig internal constructor(
    @SerialName("allowedFunctionNames")
    val allowedFunctionNamesString: List<String>,
    val mode: Mode
) {
    constructor(
        mode: Mode,
        allowedFunctionNames: List<FunctionDeclaration>
    ) : this(
        mode = mode,
        allowedFunctionNamesString = allowedFunctionNames.map { it.name }
    )
}
