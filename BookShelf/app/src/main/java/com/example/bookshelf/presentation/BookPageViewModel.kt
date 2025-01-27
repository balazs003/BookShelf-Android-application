package com.example.bookshelf.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.BookRepository
import com.example.bookshelf.model.ExtendedBook
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookPageUiState {
    data class Success(val book: ExtendedBook): BookPageUiState
    data class Error(val errorMessage: String): BookPageUiState
    data object Loading: BookPageUiState
}

class BookPageViewModel(
    private val bookRepository: BookRepository
): ViewModel() {
    var bookPageUiState: BookPageUiState by mutableStateOf(BookPageUiState.Loading)

    fun getBookDetailsFromNetwork(bookId: String) {
        viewModelScope.launch {
            bookPageUiState = try {
                val book = bookRepository.getBookDetails(bookId)
                BookPageUiState.Success(book)
            } catch (e: IOException) {
                BookPageUiState.Error(e.message ?: "IOException")
            } catch (e: HttpException) {
                BookPageUiState.Error(e.message())
            }
        }
    }
}