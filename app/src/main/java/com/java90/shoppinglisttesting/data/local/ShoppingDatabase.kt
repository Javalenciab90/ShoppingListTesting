package com.java90.shoppinglisttesting.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShoppingItem::class], version = 1)
abstract class ShoppingDatabase : RoomDatabase() {

    companion object {
        const val DATABASE_NAME = "shopping_db"
    }

    abstract fun shoppingDao(): ShoppingDao
}