package com.example.bookshelf.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bookshelf.model.ExtendedBook
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Query("select * from books")
    fun getAllBooks(): Flow<List<ExtendedBook>>

    @Query("select * from books where id like :id")
    suspend fun getBookById(id: String): ExtendedBook

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: ExtendedBook)

    @Delete
    suspend fun deleteBook(book: ExtendedBook)
}