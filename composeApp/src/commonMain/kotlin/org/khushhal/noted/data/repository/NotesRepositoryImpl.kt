package org.khushhal.noted.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.khushhal.noted.data.remote.NotesApi
import org.khushhal.noted.data.remote.dto.NoteDto
import org.khushhal.noted.domain.model.Note
import org.khushhal.noted.domain.model.User
import org.khushhal.noted.domain.repository.NotesRepository
import org.khushhal.noted.util.ResultState
import org.khushhal.noted.util.toNote
import org.khushhal.noted.util.toNoteDto

class NotesRepositoryImpl(
    private val api: NotesApi
) : NotesRepository {
    override fun getAllNotes(): Flow<ResultState<List<Note>>> = flow {
        emit(ResultState.Loading)
        try {
            val notes = api.getAllNotes().map { it.toNote() }
            emit(ResultState.Success(notes))
        } catch (e: Exception) {
            emit(ResultState.Failure(e.message ?: "Unknown error"))
        }
    }

    override suspend fun insertNote(note: Note): ResultState<String> {
        return try {
            api.createNote(note.toNoteDto())
            ResultState.Success("Note created")
        } catch (e: Exception) {
            ResultState.Failure(e.message ?: "Unknown error")
        }
    }

    override suspend fun updateNote(note: Note): ResultState<String> = try {
        api.updateNote(note.toNoteDto())
        ResultState.Success("Note updated")
    } catch (e: Exception) {
        ResultState.Failure(e.message ?: "Unknown error")
    }

    override suspend fun deleteNote(id: Int): ResultState<String> = try {
        api.deleteNote(id)
        ResultState.Success("Note deleted")
    } catch (e: Exception) {
        ResultState.Failure(e.message ?: "Unknown error")
    }
}