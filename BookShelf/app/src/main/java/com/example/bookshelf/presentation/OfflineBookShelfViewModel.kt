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

    fun getStoredBookById(booId: String): ExtendedBook =
        offlineBookRepository.getStoredBookById(booId)

    fun saveBook(book: ExtendedBook) {
        viewModelScope.launch {
            offlineBookRepository.saveBook(book)
        }
    }

    fun deleteBook(book: ExtendedBook) {
        viewModelScope.launch {
            offlineBookRepository.deleteBook(book)
        }
    }
}