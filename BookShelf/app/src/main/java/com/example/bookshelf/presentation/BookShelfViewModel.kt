package com.example.bookshelf.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookShelfApplication
import com.example.bookshelf.data.BookRepository
import com.example.bookshelf.data.MainScreenUiState
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ExtendedBook
import com.example.bookshelf.ui.screens.Page
import com.example.bookshelf.ui.screens.Pages
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookShelfUiState {
    data class Success(val bookList: List<Book>): BookShelfUiState
    data object NoResult: BookShelfUiState
    data class Error(val errorMessage: String): BookShelfUiState
    data object Loading: BookShelfUiState
    data object Initial: BookShelfUiState
}

class BookShelfViewModel(
    private val bookRepository: BookRepository
): ViewModel() {

    var bookShelfUiState: BookShelfUiState by mutableStateOf(BookShelfUiState.Loading)

    private var searchJob: Job? = null
    private val defaultQueryString = "jazz music"

    init {
        getBooksFromNetwork(defaultQueryString)
    }

    fun getBooksFromNetwork(queryString: String) {
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
}