package org.khushhal.noted.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.khushhal.noted.data.remote.dto.NoteDto
import org.khushhal.noted.domain.model.CurrentUser
import org.khushhal.noted.util.BASE_URL

class NotesApi(
    private val httpClient: HttpClient
) {
    private fun HttpRequestBuilder.authHeader() {
        CurrentUser.user?.accessToken?.let {
            header("Authorization", "Bearer $it")
        }
    }

    suspend fun getAllNotes(): List<NoteDto> =
        httpClient.get("$BASE_URL/notes") { authHeader() }.body()

    suspend fun createNote(note: NoteDto) {
        httpClient.post("$BASE_URL/notes") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(note)
        }
    }

    suspend fun updateNote(note: NoteDto) =
        httpClient.put("$BASE_URL/notes/${note.id}") {
            authHeader()
            contentType(ContentType.Application.Json)
            setBody(note)
        }

    suspend fun deleteNote(id: Int) {
        httpClient.delete("$BASE_URL/notes/$id") { authHeader() }
    }
}
