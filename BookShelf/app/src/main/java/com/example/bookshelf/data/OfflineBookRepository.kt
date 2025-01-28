package com.example.bookshelf.data

import com.example.bookshelf.data.database.BookDao
import com.example.bookshelf.model.ExtendedBook
import kotlinx.coroutines.flow.Flow

interface OfflineBookRepository {
    fun getAllStoredBooksStream(): Flow<List<ExtendedBook>>
    suspend fun getStoredBookById(bookId: String): ExtendedBook?
    suspend fun saveBook(book: ExtendedBook)
    suspend fun deleteBook(book: ExtendedBook)
}

class DbOfflineBookRepository(
    private val bookDao: BookDao
): OfflineBookRepository {
    override fun getAllStoredBooksStream(): Flow<List<ExtendedBook>> =
        bookDao.getAllBooks()

    override suspend fun getStoredBookById(bookId: String): ExtendedBook? =
        bookDao.getBookById(bookId)

    override suspend fun saveBook(book: ExtendedBook) =
        bookDao.insertBook(book)

    override suspend fun deleteBook(book: ExtendedBook) =
        bookDao.deleteBook(book)
}