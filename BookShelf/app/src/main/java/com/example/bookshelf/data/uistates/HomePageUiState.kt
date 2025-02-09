package com.example.bookshelf.data.uistates

import com.example.bookshelf.model.Book

sealed interface HomePageUiState {
    data class Success(var bookList: List<Book>): HomePageUiState
    data object NoResult: HomePageUiState
    data class Error(val errorMessage: String): HomePageUiState
    data object Loading: HomePageUiState
    data object Initial: HomePageUiState
}