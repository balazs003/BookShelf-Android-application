package com.example.bookshelf.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.bookshelf.model.ExtendedBook

@Database(entities = [ExtendedBook::class], version = 1)
@TypeConverters(Converters::class)
abstract class BookDatabase: RoomDatabase() {
    abstract fun getDao(): BookDao

    companion object {
        private var Instance: BookDatabase? = null
        fun getBookDatabase(context: Context): BookDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, BookDatabase::class.java, "book_db")
                    .fallbackToDestructiveMigration()
                    .build()
            }.also { Instance = it }
        }
    }
}