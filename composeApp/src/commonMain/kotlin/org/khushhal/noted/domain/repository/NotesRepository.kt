package org.khushhal.noted.domain.repository

import kotlinx.coroutines.flow.Flow
import org.khushhal.noted.domain.model.Note
import org.khushhal.noted.util.ResultState

interface NotesRepository {
    fun getAllNotes(): Flow<ResultState<List<Note>>>
    //fun getNoteById(id: Int): Flow<ResultState<Note>>
    suspend fun insertNote(note: Note): ResultState<String>
    suspend fun updateNote(note: Note): ResultState<String>
    suspend fun deleteNote(id: Int): ResultState<String>
    //search notes by title or content

}
