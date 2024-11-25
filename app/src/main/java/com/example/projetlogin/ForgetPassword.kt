package com.example.projetlogin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetlogin.ui.theme.ProjetLoginTheme

class ForgetPassword : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetLoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting3(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting3(modifier: Modifier = Modifier) {
    val email = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val isSuccess = remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "RESET PASSWORD",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
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
        if (isSuccess.value) {
            Text(text = "An email has been sent to reset your password.", color = Color.Green, fontSize = 14.sp)
        }
        Button(
            onClick = {
                if (email.value.isBlank()) {
                    emailError.value = "Email cannot be empty"
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                    emailError.value = "Invalid email format"
                } else {
                    emailError.value = ""
                    isSuccess.value = true
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Send")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResetPasswordPreview() {
    ProjetLoginTheme {
        Greeting3()
    }
}
