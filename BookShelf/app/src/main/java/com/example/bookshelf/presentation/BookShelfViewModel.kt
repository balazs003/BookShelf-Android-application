package com.example.bookshelf.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import retrofit2.HttpException
import com.example.bookshelf.BookShelfApplication
import com.example.bookshelf.data.BookRepository
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ExtendedBook
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface BookShelfUiState {
    data class Success(val bookList: List<Book>): BookShelfUiState
    data object NoResult: BookShelfUiState
    data class Error(val errorMessage: String): BookShelfUiState
    data object Loading: BookShelfUiState
    data object Initial: BookShelfUiState
}

sealed interface BookPageUiState {
    data class Success(val book: ExtendedBook): BookPageUiState
    data class Error(val errorMessage: String): BookPageUiState
    data object Loading: BookPageUiState
}

class BookShelfViewModel(
    private val bookRepository: BookRepository
): ViewModel() {

    var bookShelfUiState: BookShelfUiState by mutableStateOf(BookShelfUiState.Loading)
    var bookPageUiState: BookPageUiState by mutableStateOf(BookPageUiState.Loading)
    private var searchJob: Job? = null
    private val defaultQueryString = "jazz music"

    init {
        getBooks(defaultQueryString)
    }

    fun getBooks(queryString: String) {
        if (queryString.isEmpty() || queryString.isBlank()) {
            bookShelfUiState = BookShelfUiState.Initial
            searchJob?.cancel()
        } else {
            searchJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(500)
                try {
                    bookShelfUiState = BookShelfUiState.Loading
                    val books = bookRepository.getSearchResults(queryString)
                    bookShelfUiState = if (books.isNotEmpty()) BookShelfUiState.Success(books)
                                    else BookShelfUiState.NoResult
                } catch (e: IOException) {
                    bookShelfUiState = BookShelfUiState.Error(e.message ?: "IOException")
                } catch (e: HttpException) {
                    bookShelfUiState = BookShelfUiState.Error(e.message())
                }
            }
        }
    }

    fun getBookDetails(bookId: String) {
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

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application: BookShelfApplication = (this[APPLICATION_KEY]) as BookShelfApplication
                val networkBookRepository = application.container.bookRepository
                BookShelfViewModel(networkBookRepository)
            }
        }
    }
}