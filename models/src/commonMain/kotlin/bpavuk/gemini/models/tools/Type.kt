package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
enum class Type {
    TYPE_UNSPECIFIED,
    STRING,
    NUMBER,
    INTEGER,
    BOOLEAN,
    ARRAY,
    OBJECT
}
