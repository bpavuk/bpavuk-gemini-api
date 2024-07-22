package bpavuk.gemini.engineBuilder

import io.ktor.client.*

internal expect fun buildClient(config: HttpClientConfig<*>.() -> Unit): HttpClient
