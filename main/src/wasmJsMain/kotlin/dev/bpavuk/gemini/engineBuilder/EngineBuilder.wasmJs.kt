package dev.bpavuk.gemini.engineBuilder

import io.ktor.client.*
import io.ktor.client.engine.js.*

internal actual fun buildClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Js) {
    config(this)
}
