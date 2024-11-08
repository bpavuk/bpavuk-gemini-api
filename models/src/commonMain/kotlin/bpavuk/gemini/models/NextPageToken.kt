package bpavuk.gemini.models

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
public value class NextPageToken(public val token: String)
