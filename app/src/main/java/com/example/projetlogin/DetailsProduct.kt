package com.example.projetlogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetlogin.ui.theme.ProjetLoginTheme

@Composable
fun ProductCard(nom: String, prix: Double, imageName: String, description: String) {
    val context = LocalContext.current
    val imageId = resolveImageResource(imageName, context)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                val intent = Intent(context, DetailsProduct::class.java).apply {
                    putExtra("PRODUCT_NOM", nom)
                    putExtra("PRODUCT_PRIX", prix)
                    putExtra("PRODUCT_IMAGE", imageId)
                    putExtra("PRODUCT_DESCRIPTION", description)
                }
                context.startActivity(intent)
            }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = "Image du produit",
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = nom, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(text = "$prix DT", fontSize = 16.sp)
            }
        }
    }
}

fun resolveImageResource(imageName: String, context: Context): Int {
    return context.resources.getIdentifier(imageName, "drawable", context.packageName)
}

class DetailsProduct : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetLoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val name = intent.getStringExtra("PRODUCT_NOM") ?: "Nom du produit"
                    val price = intent.getDoubleExtra("PRODUCT_PRIX", 0.0)
                    val imageId = intent.getIntExtra("PRODUCT_IMAGE", R.drawable.product)
                    val description = intent.getStringExtra("PRODUCT_DESCRIPTION") ?: "Description du produit"
                    ProductDetailScreen(name, price, imageId, description, Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ProductDetailScreen(name: String, price: Double, imageId: Int, description: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = imageId),
                contentDescription = name,
                modifier = Modifier.size(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "${price} DT", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, fontSize = 16.sp)

            Button(
                onClick = {
                    Toast.makeText(context, "$name ajouté au panier!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Ajouter au panier")
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
}

@Preview(showBackground = true)
@Composable
fun ProductDetailPreview() {
    ProjetLoginTheme {
        ProductCard("Produit", 0.0, "product", "Description de produit")
    }
}
