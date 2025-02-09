package com.example.bookshelf.data.uistates

import com.example.bookshelf.model.ExtendedBook

sealed interface BookPageUiState {
    data class Success(val book: ExtendedBook): BookPageUiState
    data class Error(val errorMessage: String): BookPageUiState
    data object Loading: BookPageUiState
}