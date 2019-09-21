package com.example.shopify_memory_game.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shopify_memory_game.data.db.dao.CardDao
import com.example.shopify_memory_game.data.network.request.Card

@Database(
    entities = [Card::class],
    version = 1,
    exportSchema = false
)
abstract class MemoryDatabase : RoomDatabase() {
    abstract fun imageDao(): CardDao

    companion object {
        @Volatile
        private var instance: MemoryDatabase? = null
        private val LOCK = Any()

        // ?: if not initialized then it is going to initialize it
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                MemoryDatabase::class.java, "memory.db"
            )
                .build()
    }
}