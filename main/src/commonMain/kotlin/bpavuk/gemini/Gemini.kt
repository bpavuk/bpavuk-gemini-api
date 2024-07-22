package bpavuk.gemini

import bpavuk.gemini.engineBuilder.buildClient
import bpavuk.gemini.models.*
import bpavuk.gemini.models.surrogates.GenerateContentSurrogate
import bpavuk.gemini.models.surrogates.GetModelsSurrogate
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.Charsets
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.charsets.*
import kotlinx.serialization.json.Json

public class Gemini(
    private val baseUrl: Url = Url("https://generativelanguage.googleapis.com/v1beta/"),
    private val apiKey: String
) {
    private val client = buildClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
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

    public suspend fun generateContent(model: Model, contents: List<Content>): GenerateContentResponse {
        val result = client.post("${model.name}:generateContent") {
            setBody(GenerateContentSurrogate(contents))
        }
        return result.body()
    }
}
