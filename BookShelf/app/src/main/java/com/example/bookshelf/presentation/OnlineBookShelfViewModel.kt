package com.example.bookshelf.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.uistates.HomePageUiState
import com.example.bookshelf.data.repositories.OnlineBookRepository
import com.example.bookshelf.model.Book
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class OnlineBookShelfViewModel(
    private val onlineBookRepository: OnlineBookRepository
): ViewModel() {

    private var _homePageUiState: MutableStateFlow<HomePageUiState> = MutableStateFlow(
        HomePageUiState.Loading)
    val homePageUiState: StateFlow<HomePageUiState> = _homePageUiState.asStateFlow()

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
                _homePageUiState.value = HomePageUiState.Loading
                if (!isQueryValid(modifiedQueryString)) {
                    _homePageUiState.value = HomePageUiState.Initial
                } else {
                    val books = onlineBookRepository.getSearchResults(modifiedQueryString)
                    _homePageUiState.value = if (books.isNotEmpty()) {
                        loadFilters(books)
                        HomePageUiState.Success(books)
                    } else {
                        HomePageUiState.NoResult
                    }
                }
            } catch (e: IOException) {
                _homePageUiState.value = HomePageUiState.Error(e.message ?: "IOException")
            } catch (e: HttpException) {
                _homePageUiState.value = HomePageUiState.Error(e.message ?: "HttpException")
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
        _homePageUiState.value = if (filteredBooks.isEmpty()) HomePageUiState.NoResult
        else HomePageUiState.Success(filteredBooks)
    }

    private fun isQueryValid(queryString: String): Boolean {
        return queryString.isNotEmpty() && queryString.isNotBlank()
    }
}