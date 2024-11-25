package com.example.projetlogin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.projetlogin.ui.theme.ProjetLoginTheme
import com.example.projetlogin.FeedReaderDbHelper

class ModifierProduit : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ModifierProduitScreen()
        }
    }
}

@Composable
fun ModifierProduitScreen() {
    val context = LocalContext.current
    val dbHelper = FeedReaderDbHelper(context)

    val intent = (context as? ModifierProduit)?.intent
    val productId = intent?.getLongExtra("PRODUCT_ID", -1) ?: -1
    val nom = intent?.getStringExtra("PRODUCT_NOM") ?: ""
    val prix = intent?.getDoubleExtra("PRODUCT_PRIX", 0.0) ?: 0.0
    val description = intent?.getStringExtra("PRODUCT_DESCRIPTION") ?: ""
    val imageResource = intent?.getIntExtra("PRODUCT_IMAGE", R.drawable.product) ?: R.drawable.product

    var productName by remember { mutableStateOf(TextFieldValue(nom)) }
    var productPrice by remember { mutableStateOf(TextFieldValue(prix.toString())) }
    var productDescription by remember { mutableStateOf(TextFieldValue(description)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = "Product Image",
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = productName,
            onValueChange = { productName = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            label = { Text("Nom du produit") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            label = { Text("Prix du produit") },
            singleLine = true,
            isError = productPrice.text.isEmpty() || productPrice.text.toDoubleOrNull() == null
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            label = { Text("Description du produit") },
            singleLine = false
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (productId != -1L && productName.text.isNotEmpty() && productPrice.text.isNotEmpty() && productDescription.text.isNotEmpty()) {
                    val priceValue = productPrice.text.toDoubleOrNull() ?: 0.0
                    dbHelper.updateProduct(
                        productId,
                        productName.text,
                        priceValue,
                        productDescription.text
                    )
                    Toast.makeText(context, "Produit modifié avec succès", Toast.LENGTH_SHORT).show()
                    (context as? ModifierProduit)?.finish()
                } else {
                    Toast.makeText(context, "Tous les champs doivent être remplis correctement", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Sauvegarder les modifications")
        }
    }
}
