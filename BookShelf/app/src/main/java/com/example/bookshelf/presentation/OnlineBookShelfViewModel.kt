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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookShelfUiState {
    data class Success(var bookList: List<Book>): BookShelfUiState
    data object NoResult: BookShelfUiState
    data class Error(val errorMessage: String): BookShelfUiState
    data object Loading: BookShelfUiState
    data object Initial: BookShelfUiState
}

class OnlineBookShelfViewModel(
    private val onlineBookRepository: OnlineBookRepository
): ViewModel() {

    private var _bookShelfUiState: MutableStateFlow<BookShelfUiState> = MutableStateFlow(BookShelfUiState.Loading)
    val bookShelfUiState: StateFlow<BookShelfUiState> = _bookShelfUiState.asStateFlow()

    private var searchJob: Job? = null
    private val defaultQueryString = ('a'..'z').random().toString()

    var isFirstTry by mutableStateOf(true)

    var filterGroups by mutableStateOf(FilterGroups(emptyList()) {})

    init {
        getBooksFromNetwork(defaultQueryString)
    }

    fun getBooksFromNetwork(queryString: String) {
        //when error on launch, try again with defaultQueryString until the user modifies the query
        val modifiedQueryString = if (isFirstTry) defaultQueryString else queryString
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            filterGroups.deleteFilters()
            try {
                _bookShelfUiState.value = BookShelfUiState.Loading
                if (!isQueryValid(modifiedQueryString)) {
                    _bookShelfUiState.value = BookShelfUiState.Initial
                } else {
                    val books = onlineBookRepository.getSearchResults(modifiedQueryString)
                    _bookShelfUiState.value = if (books.isNotEmpty()) {
                        loadFilters(books)
                        BookShelfUiState.Success(books)
                    } else {
                        BookShelfUiState.NoResult
                    }
                }
            } catch (e: IOException) {
                _bookShelfUiState.value = BookShelfUiState.Error(e.message ?: "IOException")
            } catch (e: HttpException) {
                _bookShelfUiState.value = BookShelfUiState.Error(e.message ?: "HttpException")
            }
        }
    }

    fun toggleFilter(filter: Filter) = filterGroups.toggleFilter(filter)

    fun resetFilters() = filterGroups.resetFilters()

    private fun loadFilters(books: List<Book>) {
        filterGroups = FilterGroups(
            fullBookList =  books,
            onFiltersChanged = {
                updateUiState(it)
            }
        )
    }

    private fun updateUiState(filteredBooks: List<Book>) {
        _bookShelfUiState.value = if (filteredBooks.isEmpty()) BookShelfUiState.NoResult
        else BookShelfUiState.Success(filteredBooks)
    }

    private fun isQueryValid(queryString: String): Boolean {
        return queryString.isNotEmpty() && queryString.isNotBlank()
    }
}