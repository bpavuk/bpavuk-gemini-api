package dev.bpavuk.gemini.models.safety

import kotlinx.serialization.Serializable

@Serializable
public enum class BlockReason {
    BLOCK_REASON_UNSPECIFIED,
    SAFETY,
    OTHER
}