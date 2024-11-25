
package com.example.projetlogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetlogin.Utils.PREFS_NAME
import com.example.projetlogin.ui.theme.ProjetLoginTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val password = prefs.getString("password", null)

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            val intent = Intent(this, Home1::class.java)
            startActivity(intent)
        } else {
            enableEdgeToEdge()
            setContent {
                ProjetLoginTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        Greeting(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }
    val main = LocalContext.current

    val prefs = main.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    var savedmail by remember {
        mutableStateOf(prefs.getString("email", "") ?: "")
    }
    var savedpass by remember{
        mutableStateOf(prefs.getString("password", "")?: "")
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "SIGN IN",
            fontSize = 28.sp,
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

        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text("PASSWORD") },
            isError = passwordError.value.isNotEmpty(),
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (passwordError.value.isNotEmpty()) {
            Text(text = passwordError.value, color = Color.Red, fontSize = 12.sp)
        }

        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, color = Color.Blue)) {
                        append("Forget Password")
                    }
                },
                onClick = {
                    val intent = Intent(main, ForgetPassword::class.java)
                    main.startActivity(intent)
                }
            )
            Text(text = "                          ")
            ClickableText(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, color = Color.Blue)) {
                        append("Sign Up")
                    }
                },
                onClick = {
                    val intent = Intent(main, SignUp::class.java)
                    main.startActivity(intent)
                }
            )
        }

        Button(
            onClick = {
                if (email.value.isBlank()) {
                    emailError.value = "Email cannot be empty"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
                    emailError.value = "Invalid email format"
                } else {
                    emailError.value = ""
                }

                if (password.value.isBlank()) {
                    passwordError.value = "Password cannot be empty"
                } else {
                    passwordError.value = ""
                }

                if (emailError.value.isEmpty() && passwordError.value.isEmpty()) {
                    prefs.edit().putString("email",email.value).apply()
                    prefs.edit().putString("password",password.value).apply()
                    savedmail = email.value
                    savedpass = password.value
                    Toast.makeText(main, "welcome home",Toast.LENGTH_LONG).show()
                    val intent = Intent(main, Home1::class.java)
                    main.startActivity(intent)
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Sign In")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ProjetLoginTheme {
        Greeting()
    }
}

