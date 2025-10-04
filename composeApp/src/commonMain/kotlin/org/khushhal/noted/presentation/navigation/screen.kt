package org.khushhal.noted.presentation.navigation

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import org.khushhal.noted.domain.model.Note
import org.khushhal.noted.presentation.auth.LoginScreenUI
import org.khushhal.noted.presentation.auth.RegisterScreenUI
import org.khushhal.noted.presentation.notes.EditNoteScreenUI
import org.khushhal.noted.presentation.notes.NotesScreenUI

class LoginScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        LoginScreenUI(
            onLoginSuccess = {
                navigator?.replace(NotesScreen())
            },
            onRegisterClick = { navigator?.push(RegisterScreen()) }
        )
    }
}

class RegisterScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        RegisterScreenUI(
            onLoginClick = { navigator?.pop() }
        )
    }
}

class NotesScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        NotesScreenUI(
            onNoteClick = { note -> navigator?.push(EditNoteScreen(note)) },
            onCreateNote = { navigator?.push(EditNoteScreen(null)) },
            onLogout = {navigator?.replace(LoginScreen())}
        )
    }
}

data class EditNoteScreen(val note: Note?) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        EditNoteScreenUI(
            note = note,
            onBack = { navigator?.pop() }
        )
    }
}
