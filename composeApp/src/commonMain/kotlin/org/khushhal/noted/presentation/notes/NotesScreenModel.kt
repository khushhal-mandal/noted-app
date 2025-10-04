package org.khushhal.noted.presentation.notes

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.khushhal.noted.domain.model.CurrentUser
import org.khushhal.noted.domain.model.Note
import org.khushhal.noted.domain.repository.NotesRepository
import org.khushhal.noted.util.ResultState
import org.khushhal.noted.util.UserRepository

class NotesScreenModel(
    private val repository: NotesRepository,
    private val userRepository: UserRepository
) {
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(NotesState())
    val state: StateFlow<NotesState> = _state

    fun loadNotes() {
        coroutineScope.launch {
            _state.value = _state.value.copy(notes = ResultState.Loading)
            repository.getAllNotes().collect { result ->
                _state.value = _state.value.copy(notes = result)
            }
        }
    }

    /** Delete a note instantly and update the list */
    fun deleteNote(noteId: Int) {
        val currentNotes = (_state.value.notes as? ResultState.Success)?.data ?: emptyList()
        // Remove note immediately from UI
        _state.value = _state.value.copy(
            notes = ResultState.Success(currentNotes.filter { it.id != noteId }),
            noteActionState = ResultState.Loading
        )

        // Delete in repository in background
        coroutineScope.launch {
            try {
                repository.deleteNote(noteId)
                _state.value = _state.value.copy(noteActionState = ResultState.Success("Note deleted"))
                // NO need to call loadNotes() here
            } catch (e: Exception) {
                // Revert UI if deletion fails
                _state.value = _state.value.copy(
                    notes = ResultState.Success(currentNotes),
                    noteActionState = ResultState.Failure(e.message ?: "Failed to delete note")
                )
            }
        }
    }


    /** Create a new note */
    fun createNote(note: Note) {
        coroutineScope.launch {
            _state.value = _state.value.copy(noteActionState = ResultState.Loading)
            try {
                repository.insertNote(note)
                _state.value = _state.value.copy(noteActionState = ResultState.Success("Note created"))
                loadNotes() // refresh list
            } catch (e: Exception) {
                _state.value = _state.value.copy(noteActionState = ResultState.Failure(e.message ?: "Failed to create note"))
            }
        }
    }

    /** Update an existing note */
    fun updateNote(note: Note) {
        coroutineScope.launch {
            _state.value = _state.value.copy(noteActionState = ResultState.Loading)
            try {
                repository.updateNote(note)
                _state.value = _state.value.copy(noteActionState = ResultState.Success("Note updated"))
                loadNotes()
            } catch (e: Exception) {
                _state.value = _state.value.copy(noteActionState = ResultState.Failure(e.message ?: "Failed to update note"))
            }
        }
    }

    fun logout() {
        coroutineScope.launch {
            userRepository.clearUser() // clear saved user
            CurrentUser.user = null   // clear in-memory current user
        }
    }


}

data class NotesState(
    val notes: ResultState<List<Note>> = ResultState.Loading,
    val noteActionState: ResultState<String> = ResultState.Loading
)