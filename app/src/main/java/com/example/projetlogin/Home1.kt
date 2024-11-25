package com.example.projetlogin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projetlogin.Utils.PREFS_NAME
import com.example.projetlogin.ui.theme.ProjetLoginTheme

class Home1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjetLoginTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProductScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ProductScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val dbHelper = FeedReaderDbHelper(context)

    var productList by remember { mutableStateOf(emptyList<Map<String, Any>>()) }
    LaunchedEffect(Unit) {
        productList = dbHelper.readAllProducts()
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Button(
            onClick = {
                prefs.edit().remove("email").apply()
                prefs.edit().remove("password").apply()
                Toast.makeText(context, "Disconnected", Toast.LENGTH_LONG).show()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Log Out")
        }
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            items(productList) { productMap ->
                ProductItem(productMap = productMap, onDelete = {
                    val id = productMap[BaseColumns._ID] as Long
                    dbHelper.deleteProduct(id)
                    productList = dbHelper.readAllProducts()
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val intent = Intent(context, AjoutProduit::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Text(text = "Ajouter Produit")
        }
    }
}

fun resolveImageResource(image: String?, context: Context): Int {
    return if (!image.isNullOrBlank()) {
        val resourceId = context.resources.getIdentifier(image, "drawable", context.packageName)
        if (resourceId != 0) {
            resourceId
        } else {
            R.drawable.product
        }
    } else {
        R.drawable.product
    }
}

@Composable
fun ProductItem(productMap: Map<String, Any>, modifier: Modifier = Modifier, onDelete: () -> Unit) {
    val context = LocalContext.current
    val nom = productMap[ProductContract.ProductEntry.TABLE_COLUMN_NOM] as String
    val prix = productMap[ProductContract.ProductEntry.TABLE_COLUMN_PRIX] as Double
    val description = productMap[ProductContract.ProductEntry.TABLE_COLUMN_DESCRPTION] as String
    val imageName = productMap[ProductContract.ProductEntry.TABLE_COLUMN_IMAGE] as? String

    val id = productMap[BaseColumns._ID] as? Long
    if (id == null) {
        Log.e("ProductItem", "Product ID is null, cannot delete")
        return
    }

    Card(
        modifier = modifier
            .padding(10.dp)
            .clickable {
                val intent = Intent(context, DetailsProduct::class.java).apply {
                    putExtra("PRODUCT_NOM", nom)
                    putExtra("PRODUCT_PRIX", prix)
                    putExtra("PRODUCT_IMAGE", resolveImageResource(imageName, context))
                    putExtra("PRODUCT_DESCRIPTION", description)
                }
                context.startActivity(intent)
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = nom,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = painterResource(id = resolveImageResource(imageName, context)),
                contentDescription = nom,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${prix} DT",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val intent = Intent(context, ModifierProduit::class.java).apply {
                        putExtra("PRODUCT_ID", id)
                        putExtra("PRODUCT_NOM", nom)
                        putExtra("PRODUCT_PRIX", prix)
                        putExtra("PRODUCT_DESCRIPTION", description)
                        putExtra("PRODUCT_IMAGE", resolveImageResource(imageName, context))
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Modifier")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    Log.d("DeleteButton", "Deleting product with name: $nom and ID: $id")
                    onDelete()
                },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(text = "Supprimer")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    ProjetLoginTheme {
        ProductScreen()
    }
}
