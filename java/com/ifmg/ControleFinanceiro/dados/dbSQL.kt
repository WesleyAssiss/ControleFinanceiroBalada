package com.ifmg.ControleFinanceiro.dados

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ifmg.ControleFinanceiro.modelo.Product
import java.util.*

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "finance.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_PRODUCTS = "products"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_VALUE = "value"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_IS_ENTRY = "isEntry"

        private const val CREATE_TABLE_PRODUCTS =
            "CREATE TABLE $TABLE_PRODUCTS (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NAME TEXT," +
                    "$COLUMN_VALUE REAL," +
                    "$COLUMN_DATE TEXT," +
                    "$COLUMN_IS_ENTRY INTEGER)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PRODUCTS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        onCreate(db)
    }

    fun addProduct(product: Product) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, product.name)
            put(COLUMN_VALUE, product.value)
            put(COLUMN_DATE, product.date)
            put(COLUMN_IS_ENTRY, if (product.isEntry) 1 else 0)
        }

        writableDatabase.insert(TABLE_PRODUCTS, null, values)
    }

    @SuppressLint("Range")
    fun getProducts(): List<Product> {
        val productList = mutableListOf<Product>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_PRODUCTS", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val value = cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE))
                val date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                val isEntry = cursor.getInt(cursor.getColumnIndex(COLUMN_IS_ENTRY)) == 1

                val product = Product(id, name, value, date, isEntry)
                productList.add(product)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return productList
    }
    fun clearAllData() {
        writableDatabase.execSQL("DELETE FROM $TABLE_PRODUCTS")
    }

}