package dev.bpavuk.gemini

import dev.bpavuk.gemini.models.safety.HarmBlockThreshold
import dev.bpavuk.gemini.models.safety.HarmCategory
import dev.bpavuk.gemini.models.safety.SafetySetting

public interface SafetySettingsBuilder {
    public var harassment: HarmBlockThreshold
    public var hate: HarmBlockThreshold
    public var sexuallyExplicit: HarmBlockThreshold
    public var dangerousContent: HarmBlockThreshold
}

public data class SafetySettingsImpl(
    override var harassment: HarmBlockThreshold = HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE,
    override var hate: HarmBlockThreshold = HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE,
    override var sexuallyExplicit: HarmBlockThreshold = HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE,
    override var dangerousContent: HarmBlockThreshold = HarmBlockThreshold.BLOCK_MEDIUM_AND_ABOVE
) : SafetySettingsBuilder

public fun safetySettings(builder: SafetySettingsBuilder.() -> Unit): List<SafetySetting> {
    val impl = SafetySettingsImpl()
    return impl.apply(builder).let {
        listOf(
            SafetySetting(HarmCategory.HARM_CATEGORY_DANGEROUS_CONTENT, it.dangerousContent),
            SafetySetting(HarmCategory.HARM_CATEGORY_HARASSMENT, it.dangerousContent),
            SafetySetting(HarmCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT, it.sexuallyExplicit),
            SafetySetting(HarmCategory.HARM_CATEGORY_HATE_SPEECH, it.hate)
        )
    }
}
