package bpavuk.gemini.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class NextPageToken(val token: String)
