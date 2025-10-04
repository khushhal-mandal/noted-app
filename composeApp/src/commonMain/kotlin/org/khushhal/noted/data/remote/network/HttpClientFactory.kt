package org.khushhal.noted.data.remote.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientFactory {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    encodeDefaults = true
                    isLenient = true
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }
}