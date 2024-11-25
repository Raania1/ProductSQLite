package com.example.projetlogin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.projetlogin.ui.theme.ProjetLoginTheme

class AjoutProduit : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetLoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AddProductScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AddProductScreen(modifier: Modifier = Modifier) {
    var nom by remember { mutableStateOf("") }
    var prix by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }

    val context = LocalContext.current
    val dbHelper = FeedReaderDbHelper(context)

    val addProductToDatabase = {
        if (nom.isNotBlank() && prix.isNotBlank() && description.isNotBlank()) {
            val parsedPrix = prix.toDoubleOrNull()
            if (parsedPrix != null) {
                dbHelper.insertProduct(nom, parsedPrix, description, image)
                Toast.makeText(context, "Produit ajouté avec succès", Toast.LENGTH_SHORT).show()
                nom = ""
                prix = ""
                description = ""
                image = ""
            } else {
                Toast.makeText(context, "Veuillez entrer un prix valide", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TextField(
            value = nom,
            onValueChange = { nom = it },
            label = { Text("Nom du produit") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = prix,
            onValueChange = { prix = it },
            label = { Text("Prix du produit") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description du produit") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = image,
            onValueChange = { image = it },
            label = { Text("URL de l'image (facultatif)") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = { addProductToDatabase() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Ajouter le produit")
        }
        Button(
            onClick = {
                val intent = Intent(context, Home1::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(text = " <- Produits")
        }

    }
}

@Preview(showBackground = true)
@Composable
fun AddProductScreenPreview() {
    ProjetLoginTheme {
        AddProductScreen()
    }
}
