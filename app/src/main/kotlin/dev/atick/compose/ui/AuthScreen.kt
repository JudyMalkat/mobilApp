package dev.atick.compose.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import dev.atick.compose.R

@Composable
fun AuthScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("UserData", Context.MODE_PRIVATE)

    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLoginMode) stringResource(R.string.auth_login_title) else stringResource(R.string.auth_signup_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.auth_email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.auth_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show()
                    return@Button
                }

                if (isLoginMode) {
                    val savedPassword = sharedPreferences.getString(email, null)
                    if (savedPassword == null) {
                        Toast.makeText(context, context.getString(R.string.error_account_not_found), Toast.LENGTH_SHORT).show()
                    } else if (savedPassword == password) {
                        Toast.makeText(context, context.getString(R.string.success_login), Toast.LENGTH_SHORT).show()
                        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
                        onLoginSuccess()
                    } else {
                        Toast.makeText(context, context.getString(R.string.error_wrong_password), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (sharedPreferences.contains(email)) {
                        Toast.makeText(context, context.getString(R.string.error_email_exists), Toast.LENGTH_SHORT).show()
                    } else {
                        sharedPreferences.edit().putString(email, password).apply()
                        Toast.makeText(context, context.getString(R.string.success_signup), Toast.LENGTH_SHORT).show()
                        isLoginMode = true
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(if (isLoginMode) stringResource(R.string.auth_login_btn) else stringResource(R.string.auth_signup_btn))
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = { isLoginMode = !isLoginMode }) {
            Text(if (isLoginMode) stringResource(R.string.prompt_signup) else stringResource(R.string.prompt_login))
        }
    }
}