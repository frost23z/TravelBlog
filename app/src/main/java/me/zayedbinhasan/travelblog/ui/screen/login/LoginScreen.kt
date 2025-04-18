package me.zayedbinhasan.travelblog.ui.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = state.username,
                onValueChange = { onIntent(LoginIntent.UsernameChanged(it)) },
                enabled = state.enabled,
                label = { Text("Username") },
                placeholder = { Text("Enter your username") },
                supportingText = {
                    state.usernameError?.let {
                        Text(
                            text = it,
                            color = Color.Red
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.size(24.dp))
            TextField(
                value = state.password,
                onValueChange = { onIntent(LoginIntent.PasswordChanged(it)) },
                enabled = state.enabled,
                label = { Text("Password") },
                placeholder = { Text("Enter your password") },
                supportingText = {
                    state.passwordError?.let {
                        Text(
                            text = it,
                            color = Color.Red
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Password
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.size(24.dp))
            Button(
                onClick = { onIntent(LoginIntent.LoginClicked) },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.enabled,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "Login", style = MaterialTheme.typography.titleLarge)
            }

            if (state.showDialog) {
                AlertDialog(
                    onDismissRequest = { onIntent(LoginIntent.DismissDialog) },
                    title = { Text("Success") },
                    text = { Text("Login successful!") },
                    confirmButton = {
                        Button(onClick = { onIntent(LoginIntent.DismissDialog) }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    LoginScreen(state = LoginState(), onIntent = {})
}