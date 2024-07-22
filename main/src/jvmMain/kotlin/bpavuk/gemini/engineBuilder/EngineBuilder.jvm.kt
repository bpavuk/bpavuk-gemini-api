package bpavuk.gemini.engineBuilder

import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.apache5.*
import io.ktor.client.plugins.logging.*

internal actual fun buildClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Apache5) {
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Napier.v("Http client", null, message)
            }
        }
        level = LogLevel.NONE
    }

    config(this)
}.also { Napier.base(DebugAntilog()) }
