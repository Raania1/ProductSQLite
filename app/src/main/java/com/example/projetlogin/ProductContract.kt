package com.example.projetlogin

import android.provider.BaseColumns

object ProductContract{
    object ProductEntry : BaseColumns{
        const val TABLE_NAME = "product"
        const val TABLE_COLUMN_NOM ="nom"
        const val  TABLE_COLUMN_PRIX="prix"
        const val TABLE_COLUMN_DESCRPTION="description"
        const val TABLE_COLUMN_IMAGE="image"
    }
    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${ProductEntry.TABLE_NAME}("+
                "${BaseColumns._ID} INTEGER PRIMARY KEY,"+
                "${ProductEntry.TABLE_COLUMN_NOM} TEXT NOT NULL,"+
                "${ProductEntry.TABLE_COLUMN_PRIX} REAL NOT NULL,"+
                "${ProductEntry.TABLE_COLUMN_DESCRPTION} TEXT NOT NULL,"+
                "${ProductEntry.TABLE_COLUMN_IMAGE} TEXT)"
    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ProductEntry.TABLE_NAME}"
}