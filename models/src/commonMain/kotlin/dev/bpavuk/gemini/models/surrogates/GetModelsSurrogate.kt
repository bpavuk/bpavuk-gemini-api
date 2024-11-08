package dev.bpavuk.gemini.models.surrogates

import dev.bpavuk.gemini.models.Model
import dev.bpavuk.gemini.models.NextPageToken
import kotlinx.serialization.Serializable

@Serializable
public data class GetModelsSurrogate(
    val models: List<Model>,
    val nextPageToken: NextPageToken? = null
)
