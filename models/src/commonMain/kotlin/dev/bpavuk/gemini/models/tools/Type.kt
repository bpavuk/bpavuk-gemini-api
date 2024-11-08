package dev.bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
public enum class Type {
    TYPE_UNSPECIFIED,
    STRING,
    NUMBER,
    INTEGER,
    BOOLEAN,
    ARRAY,
    OBJECT
}
