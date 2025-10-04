package org.khushhal.noted.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.khushhal.noted.data.remote.dto.AuthRequest
import org.khushhal.noted.util.BASE_URL

class AuthApi(private val httpClient: HttpClient) {
    suspend fun register(email: String, password: String): String =
        httpClient.post("$BASE_URL/register") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(email, password))
        }.body<Map<String, String>>()["msg"] ?: ""

    suspend fun login(email: String, password: String): String? {
        val response = httpClient.post("$BASE_URL/login") {
            contentType(ContentType.Application.Json)
            setBody(AuthRequest(email, password))
        }.body<Map<String, String>>()
        return response["access_token"]
    }
}

