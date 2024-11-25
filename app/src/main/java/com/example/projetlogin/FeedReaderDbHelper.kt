package com.example.projetlogin

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.projetlogin.ProductContract.SQL_CREATE_ENTRIES
import com.example.projetlogin.ProductContract.SQL_DELETE_ENTRIES

class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    constructor(context: Context, name: String, version: Int) : this(context)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 3
        const val DATABASE_NAME = "products.db"
    }

    fun insertProduct(nom: String, prix: Double, description: String, image: String?) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.TABLE_COLUMN_NOM, nom)
            put(ProductContract.ProductEntry.TABLE_COLUMN_PRIX, prix)
            put(ProductContract.ProductEntry.TABLE_COLUMN_DESCRPTION, description)
            put(ProductContract.ProductEntry.TABLE_COLUMN_IMAGE, image)
        }
        db.insert(ProductContract.ProductEntry.TABLE_NAME, null, values)
        db.close()

    }
    fun readAllProducts(): List<Map<String, Any>> {
        val db = readableDatabase
        val cursor = db.query(
            ProductContract.ProductEntry.TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val products = mutableListOf<Map<String, Any>>()

        val idIndex = cursor.getColumnIndex(BaseColumns._ID)
        val nomIndex = cursor.getColumnIndex(ProductContract.ProductEntry.TABLE_COLUMN_NOM)
        val prixIndex = cursor.getColumnIndex(ProductContract.ProductEntry.TABLE_COLUMN_PRIX)
        val descriptionIndex = cursor.getColumnIndex(ProductContract.ProductEntry.TABLE_COLUMN_DESCRPTION)
        val imageIndex = cursor.getColumnIndex(ProductContract.ProductEntry.TABLE_COLUMN_IMAGE)

        if (idIndex == -1 || nomIndex == -1 || prixIndex == -1 || descriptionIndex == -1 || imageIndex == -1) {
            Log.e("ReadAllProducts", "Une ou plusieurs colonnes n'ont pas été trouvées.")
            cursor.close()
            return emptyList()
        }

        while (cursor.moveToNext()) {
            val productMap = mutableMapOf<String, Any>()
            val id = cursor.getLong(idIndex)
            val nom = cursor.getString(nomIndex)
            val prix = cursor.getDouble(prixIndex)
            val description = cursor.getString(descriptionIndex)
            val image = cursor.getString(imageIndex)

            productMap[BaseColumns._ID] = id
            productMap[ProductContract.ProductEntry.TABLE_COLUMN_NOM] = nom
            productMap[ProductContract.ProductEntry.TABLE_COLUMN_PRIX] = prix
            productMap[ProductContract.ProductEntry.TABLE_COLUMN_DESCRPTION] = description
            productMap[ProductContract.ProductEntry.TABLE_COLUMN_IMAGE] = image ?: ""

            Log.d("ReadAllProducts", "Produit ID: $id, Nom: $nom, Prix: $prix")

            products.add(productMap)
        }
        cursor.close()
        return products
    }
    fun updateProduct(id: Long, nom: String, prix: Double, description: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(ProductContract.ProductEntry.TABLE_COLUMN_NOM, nom)
            put(ProductContract.ProductEntry.TABLE_COLUMN_PRIX, prix)
            put(ProductContract.ProductEntry.TABLE_COLUMN_DESCRPTION, description)
        }
        db.update(
            ProductContract.ProductEntry.TABLE_NAME,
            values,
            "${BaseColumns._ID} = ?",
            arrayOf(id.toString())
        )
        db.close()

    }
    fun deleteProduct(productId: Long) {
        val db = writableDatabase
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(productId.toString())
        Log.d("DeleteProduct", "Deleting product with ID: $productId")
        db.delete(ProductContract.ProductEntry.TABLE_NAME, selection, selectionArgs)
        db.close()
    }


}
