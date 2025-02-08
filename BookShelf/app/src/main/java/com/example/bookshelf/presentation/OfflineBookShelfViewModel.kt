package com.example.bookshelf.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.OfflineBookRepository
import com.example.bookshelf.model.ExtendedBook
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class OfflineBookShelfViewModel(
    private val offlineBookRepository: OfflineBookRepository
): ViewModel() {
    fun getAllStoredBooks(): Flow<List<ExtendedBook>> =
        offlineBookRepository.getAllStoredBooksStream()

    fun deleteBooks(bookIds: List<String>) {
        viewModelScope.launch {
            bookIds.forEach { id ->
                val book = offlineBookRepository.getStoredBookById(id)
                book?.let {
                    offlineBookRepository.deleteBook(book)
                }
            }
        }
    }

    suspend fun getBookById(bookId: String): ExtendedBook? =
        offlineBookRepository.getStoredBookById(bookId)
}