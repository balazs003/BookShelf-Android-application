package com.example.bookshelf.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.OnlineBookRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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
    private val onlineBookRepository: OnlineBookRepository
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
                    val books = onlineBookRepository.getSearchResults(queryString)
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