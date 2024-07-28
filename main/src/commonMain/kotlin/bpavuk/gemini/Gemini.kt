package bpavuk.gemini

import bpavuk.gemini.engineBuilder.buildClient
import bpavuk.gemini.models.*
import bpavuk.gemini.models.safety.SafetySetting
import bpavuk.gemini.models.surrogates.GenerateContentSurrogate
import bpavuk.gemini.models.surrogates.GetModelsSurrogate
import bpavuk.gemini.models.tools.EmptyResult
import bpavuk.gemini.models.tools.ExpectedFunctionResult
import bpavuk.gemini.models.tools.Tool
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.PolymorphicModuleBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

public class Gemini(
    private val baseUrl: Url = Url("https://generativelanguage.googleapis.com/v1beta/"),
    private val apiKey: String,
    argumentSerializers: PolymorphicModuleBuilder<ExpectedFunctionResult>.() -> Unit = {}
) {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        allowTrailingComma = true
        serializersModule = SerializersModule {
            polymorphic(ExpectedFunctionResult::class) {
                argumentSerializers()
                defaultDeserializer { EmptyResult.serializer() }
            }
        }
    }
    private val client = buildClient {
        install(ContentNegotiation) {
            json(json)
        }
        install(DefaultRequest)

        defaultRequest {
            url {
                protocol = baseUrl.protocol
                host = baseUrl.host
                path(baseUrl.encodedPath)
                parameters.append("key", apiKey)
            }
            header("Content-Type", "application/json")
            header("Accept", "application/json")
        }

        Charsets {
            register(Charsets.UTF_8)

            sendCharset = Charsets.UTF_8
            responseCharsetFallback = Charsets.UTF_8
        }
    }

    public suspend fun getModels(pageSize: Int = 50, pageToken: String? = null): ModelPage {
        val result = client.get("models") {
            url {
                parameters.append("pageSize", pageSize.toString())
                pageToken?.let { parameters.append("pageToken", pageToken) }
            }
        }
        try {
            val serializedResult = result.body<GetModelsSurrogate>()
            return Pair(serializedResult.models, serializedResult.nextPageToken)
        } catch (e: JsonConvertException) {
            throw result.body<GeminiRequestErrorSurrogate>().error
        }
    }

    public suspend fun generateContent(
        model: Model,
        contents: List<Content>,
        tools: List<Tool>? = null,
        safetySettings: List<SafetySetting>? = null,
        systemInstructions: String? = null
    ): GenerateContentResponse {
        val result = client.post("${model.name}:generateContent") {
            val systemInstructionContent = systemInstructions?.let {
                content(role = "system") {
                    text(it)
                }
            }
            setBody(GenerateContentSurrogate(
                contents,
                tools,
                safetySettings = safetySettings,
                systemInstruction = systemInstructionContent
            ))
        }
        return result.body()
    }
}
