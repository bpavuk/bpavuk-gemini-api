package bpavuk.gemini.models.safety

import kotlinx.serialization.Serializable

@Serializable
enum class BlockReason {
    BLOCK_REASON_UNSPECIFIED,
    SAFETY,
    OTHER
}