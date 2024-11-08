package dev.bpavuk.gemini.engineBuilder

import io.ktor.client.*
import io.ktor.client.engine.darwin.*

internal actual fun buildClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) {
    config(this)
}
