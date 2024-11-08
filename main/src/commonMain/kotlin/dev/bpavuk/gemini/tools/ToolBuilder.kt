package dev.bpavuk.gemini.tools

import dev.bpavuk.gemini.models.tools.FunctionDeclaration
import dev.bpavuk.gemini.models.tools.Schema
import dev.bpavuk.gemini.models.tools.Tool


public interface ToolBuilder {
    /**
     * Defines a new function.
     * @param name Defines a name for the function.
     * @param parameters See [Schema] for defining parameters
     * @param description This should be a short description for AI to understand
     * the purpose of this function
     */
    public fun function(name: String, parameters: Schema? = null, description: String)
}

private class ToolBuilderImpl : ToolBuilder {
    val functionStorage: MutableList<FunctionDeclaration> = mutableListOf()

    /**
     * Adds new [FunctionDeclaration] to [functionStorage]
     */
    override fun function(name: String, parameters: Schema?, description: String) {
        functionStorage.add(FunctionDeclaration(name, description, parameters))
    }
    
    fun buildTool(): Tool = Tool(functionStorage)
}

public fun tool(builder: ToolBuilder.() -> Unit): Tool =
    ToolBuilderImpl().apply(builder).buildTool()
