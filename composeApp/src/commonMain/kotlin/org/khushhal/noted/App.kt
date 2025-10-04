import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.khushhal.noted.presentation.auth.AuthScreenModel
import org.khushhal.noted.presentation.navigation.LoginScreen
import org.khushhal.noted.presentation.navigation.NotesScreen
import org.khushhal.noted.util.AppDatabaseConstructor
import org.khushhal.noted.util.ResultState
import org.khushhal.noted.util.UserDao
import org.khushhal.noted.util.UserRepository
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val viewModel: AuthScreenModel = koinInject()
    var isUserLoggedIn by remember { mutableStateOf<Boolean?>(null) }
    val state by viewModel.state.collectAsState()

    // Step 1: Load saved user
    LaunchedEffect(Unit) {
        val user = viewModel.userPreferences.getUser()
        if (user != null && !user.email.isNullOrBlank() && !user.password.isNullOrBlank()) {
            viewModel.login(user.email, user.password)
        } else {
            isUserLoggedIn = false
        }
    }

    // Step 2: Wait for login result
    LaunchedEffect(state.loginState) {
        when (val login = state.loginState) {
            is ResultState.Success -> isUserLoggedIn = true
            is ResultState.Failure -> isUserLoggedIn = false
            else -> {}
        }
    }

    MaterialTheme {
        when (isUserLoggedIn) {
            null -> Text("Loading...", modifier = Modifier.fillMaxSize())
            true -> Navigator(NotesScreen()) { SlideTransition(it) }
            false -> Navigator(LoginScreen()) { SlideTransition(it) }
        }
    }
}


