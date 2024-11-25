package com.example.projetlogin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetlogin.ui.theme.ProjetLoginTheme

class SignUp : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetLoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting2(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting2(modifier: Modifier = Modifier) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val nameError = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val confirmPasswordError = remember { mutableStateOf("") }

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "SIGN UP",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("YOUR NAME") },
            isError = nameError.value.isNotEmpty(),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (nameError.value.isNotEmpty()) {
            Text(text = nameError.value, color = Color.Red, fontSize = 12.sp)
        }
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text("YOUR EMAIL") },
            isError = emailError.value.isNotEmpty(),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (emailError.value.isNotEmpty()) {
            Text(text = emailError.value, color = Color.Red, fontSize = 12.sp)
        }
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("PASSWORD") },
            visualTransformation = PasswordVisualTransformation(),
            isError = passwordError.value.isNotEmpty(),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (passwordError.value.isNotEmpty()) {
            Text(text = passwordError.value, color = Color.Red, fontSize = 12.sp)
        }
        OutlinedTextField(
            value = confirmPassword.value,
            onValueChange = { confirmPassword.value = it },
            label = { Text("CONFIRM PASSWORD") },
            visualTransformation = PasswordVisualTransformation(),
            isError = confirmPasswordError.value.isNotEmpty(),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (confirmPasswordError.value.isNotEmpty()) {
            Text(text = confirmPasswordError.value, color = Color.Red, fontSize = 12.sp)
        }
        Button(
            onClick = {
                if (name.value.isBlank()) {
                    nameError.value = "Name cannot be empty"
                } else {
                    nameError.value = ""
                }

                if (email.value.isBlank()) {
                    emailError.value = "Email cannot be empty"
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                    emailError.value = "Invalid email format"
                } else {
                    emailError.value = ""
                }

                if (password.value.isBlank()) {
                    passwordError.value = "Password cannot be empty"
                } else if (password.value.length < 6) {
                    passwordError.value = "Password must be at least 6 characters"
                } else {
                    passwordError.value = ""
                }

                if (confirmPassword.value.isBlank()) {
                    confirmPasswordError.value = "Confirm password cannot be empty"
                } else if (confirmPassword.value != password.value) {
                    confirmPasswordError.value = "Passwords do not match"
                } else {
                    confirmPasswordError.value = ""
                }
                if (nameError.value.isEmpty() && emailError.value.isEmpty() &&
                    passwordError.value.isEmpty() && confirmPasswordError.value.isEmpty()) {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Sign Up")
        }
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                fontSize = 12.sp
            )
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, color = Color.Blue)) {
                        append("Sign In")
                    }
                },
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpPreview() {
    ProjetLoginTheme {
        Greeting2()
    }
}
