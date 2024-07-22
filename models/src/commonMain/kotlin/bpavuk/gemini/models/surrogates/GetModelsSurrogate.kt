package bpavuk.gemini.models.surrogates

import bpavuk.gemini.models.Model
import bpavuk.gemini.models.NextPageToken
import kotlinx.serialization.Serializable

@Serializable
data class GetModelsSurrogate(
    val models: List<Model>,
    val nextPageToken: NextPageToken? = null
)
