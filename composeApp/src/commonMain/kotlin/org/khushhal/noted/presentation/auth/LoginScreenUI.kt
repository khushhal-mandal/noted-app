package org.khushhal.noted.presentation.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.khushhal.noted.util.ResultState
import org.koin.compose.koinInject

@Composable
fun LoginScreenUI(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val authScreenModel: AuthScreenModel = koinInject()
    val state by authScreenModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Brown theme colors
    val backgroundColor = Color(0xFFF5EDE0) // light beige
    val primaryBrown = Color(0xFF6D4C41)    // chocolate brown
    val accentBrown = Color(0xFF8D6E63)     // lighter brown

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = backgroundColor
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Welcome Back",
                    style = MaterialTheme.typography.headlineMedium,
                    color = primaryBrown
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Login to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = accentBrown
                )
                Spacer(Modifier.height(32.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrown,
                        focusedLabelColor = primaryBrown
                    )
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryBrown,
                        focusedLabelColor = primaryBrown
                    )
                )

                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = { authScreenModel.login(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrown,
                        contentColor = Color.White
                    )
                ) {
                    Text("Login")
                }

                Spacer(Modifier.height(12.dp))
                TextButton(onClick = { onRegisterClick() }) {
                    Text(
                        "Don't have an account? Register",
                        color = accentBrown
                    )
                }

                Spacer(Modifier.height(16.dp))
                when (val result = state.loginState) {
                    is ResultState.Loading -> {}
                    is ResultState.Success -> {
                        LaunchedEffect(result) {
                            onLoginSuccess()
                        }
                    }
                    is ResultState.Failure -> Text(
                        "Error: ${result.message}",
                        color = MaterialTheme.colorScheme.error
                    )
                    else -> {}
                }
            }
        }
    }
}