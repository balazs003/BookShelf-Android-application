package com.example.bookshelf.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.example.bookshelf.presentation.BookShelfUiState
import com.example.bookshelf.presentation.OnlineBookShelfViewModel
import com.example.bookshelf.ui.components.ChipContainer
import com.example.bookshelf.ui.screens.states.ErrorScreen
import com.example.bookshelf.ui.screens.states.InitialScreen
import com.example.bookshelf.ui.screens.states.LoadingScreen
import com.example.bookshelf.ui.screens.states.NoResultScreen
import com.example.bookshelf.ui.screens.states.HomePageResultScreen

@Composable
fun HomeScreen(
    viewModel: OnlineBookShelfViewModel,
    retryAction: () -> Unit,
    onBookClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler(enabled = true) {
        onBackClick()
    }
    Column {
        ChipContainer(
            viewModel = viewModel
        )
        AnimatedContent(
            targetState = viewModel.bookShelfUiState.collectAsState().value
        ) { targetState ->
            when (targetState) {
                is BookShelfUiState.Initial ->
                    InitialScreen()

                is BookShelfUiState.Loading ->
                    LoadingScreen()

                is BookShelfUiState.NoResult ->
                    NoResultScreen()

                is BookShelfUiState.Error ->
                    ErrorScreen(
                        targetState.errorMessage,
                        retryAction
                    )

                is BookShelfUiState.Success ->
                    Column {
                        HomePageResultScreen(
                            bookList = targetState.bookList,
                            onBookClick = onBookClick
                        )
                    }
            }
        }
    }
}