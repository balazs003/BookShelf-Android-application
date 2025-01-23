package com.example.bookshelf.ui.screens

import androidx.compose.runtime.Composable
import com.example.bookshelf.presentation.BookPageUiState

@Composable
fun BookScreen(
    bookPageUiState: BookPageUiState,
    retryAction: () -> Unit
) {
    when (bookPageUiState) {
        is BookPageUiState.Loading ->
            LoadingScreen()
        is BookPageUiState.Error ->
            ErrorScreen(
                bookPageUiState.errorMessage,
                retryAction
            )
        is BookPageUiState.Success ->
            BookPageResultScreen(bookPageUiState.book)
    }
}