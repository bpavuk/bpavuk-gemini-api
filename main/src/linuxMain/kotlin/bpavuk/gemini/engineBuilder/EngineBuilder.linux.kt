package bpavuk.gemini.engineBuilder

import io.ktor.client.*
import io.ktor.client.engine.cio.*

internal actual fun buildClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(CIO) {
    config(this)
}
