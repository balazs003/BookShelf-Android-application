package com.example.bookshelf.ui.screens

import androidx.compose.runtime.Composable
import com.example.bookshelf.presentation.BookShelfUiState
import com.example.bookshelf.ui.screens.states.ErrorScreen
import com.example.bookshelf.ui.screens.states.HomePageResultScreen
import com.example.bookshelf.ui.screens.states.InitialScreen
import com.example.bookshelf.ui.screens.states.LoadingScreen
import com.example.bookshelf.ui.screens.states.NoResultScreen

@Composable
fun HomeScreen(
    bookShelfUiState: BookShelfUiState,
    retryAction: () -> Unit,
    onBookClick: (String) -> Unit
) {
    when (bookShelfUiState) {
        is BookShelfUiState.Initial ->
            InitialScreen()

        is BookShelfUiState.Loading ->
            LoadingScreen()

        is BookShelfUiState.NoResult ->
            NoResultScreen()

        is BookShelfUiState.Error ->
            ErrorScreen(
                bookShelfUiState.errorMessage,
                retryAction
            )

        is BookShelfUiState.Success ->
            HomePageResultScreen(
                bookList = bookShelfUiState.bookList,
                onBookClick = onBookClick
            )
    }
}