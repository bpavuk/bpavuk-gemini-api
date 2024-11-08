package bpavuk.gemini.models

import kotlinx.serialization.Serializable

@Serializable
public enum class Outcome {
    OUTCOME_UNSPECIFIED,
    OUTCOME_OK,
    OUTCOME_FAILED,
    OUTCOME_DEADLINE_EXCEEDED
}
