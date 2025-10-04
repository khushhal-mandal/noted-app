package org.khushhal.noted.presentation.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import org.khushhal.noted.util.ResultState
import org.koin.compose.koinInject

@Composable
fun RegisterScreenUI(
    onLoginClick: () -> Unit
) {
    val authScreenModel: AuthScreenModel = koinInject()
    val state by authScreenModel.state.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Brown theme colors (same as LoginScreenUI)
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
                    "Create Account",
                    style = MaterialTheme.typography.headlineMedium,
                    color = primaryBrown
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Register to get started",
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
                    onClick = { authScreenModel.register(email, password) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryBrown,
                        contentColor = Color.White
                    )
                ) {
                    Text("Register")
                }

                Spacer(Modifier.height(12.dp))
                TextButton(onClick = { onLoginClick() }) {
                    Text(
                        "Already have an account? Login",
                        color = accentBrown
                    )
                }

                Spacer(Modifier.height(16.dp))
                when (val result = state.registerState) {
                    is ResultState.Loading -> {}
                    is ResultState.Success -> {
                        Text(
                            "Registration successful",
                            color = primaryBrown
                        )
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
