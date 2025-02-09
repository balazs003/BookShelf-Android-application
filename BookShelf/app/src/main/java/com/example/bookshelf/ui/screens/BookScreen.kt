package com.example.bookshelf.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ScrollState
import androidx.compose.runtime.Composable
import com.example.bookshelf.data.uistates.BookPageUiState
import com.example.bookshelf.ui.screens.states.BookPageResultScreen
import com.example.bookshelf.ui.screens.states.ErrorScreen
import com.example.bookshelf.ui.screens.states.LoadingScreen

@Composable
fun BookScreen(
    bookPageUiState: BookPageUiState,
    scrollState: ScrollState,
    retryAction: () -> Unit,
    onBackPressed: () -> Unit
) {
    BackHandler(true) {
        onBackPressed()
    }
    when (bookPageUiState) {
        is BookPageUiState.Loading ->
            LoadingScreen()
        is BookPageUiState.Error ->
            ErrorScreen(
                bookPageUiState.errorMessage,
                retryAction
            )
        is BookPageUiState.Success ->
            BookPageResultScreen(bookPageUiState.book, scrollState)
    }
}