package bpavuk.gemini.models.tools

import kotlinx.serialization.Serializable

@Serializable
public data class Schema(
    val type: Type,
    val description: String? = null,
    val nullable: Boolean = true,
    val properties: Map<String, Schema>? = null,
    val required: List<String>? = null,
    val items: Schema? = null
) {
    init {
        when {
            type == Type.TYPE_UNSPECIFIED -> error("TYPE_UNSPECIFIED should not be used")
            (properties != null || required != null) && type != Type.OBJECT -> error("Only objects can have properties")
            type != Type.ARRAY && items != null -> error("Only arrays can have items")
        }
    }
}
