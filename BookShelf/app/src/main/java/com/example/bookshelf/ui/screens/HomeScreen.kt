package com.example.bookshelf.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.bookshelf.presentation.BookShelfUiState
import com.example.bookshelf.presentation.FilterGroups
import com.example.bookshelf.ui.components.ChipContainer
import com.example.bookshelf.ui.screens.states.ErrorScreen
import com.example.bookshelf.ui.screens.states.ResultScreen
import com.example.bookshelf.ui.screens.states.InitialScreen
import com.example.bookshelf.ui.screens.states.LoadingScreen
import com.example.bookshelf.ui.screens.states.NoResultScreen

@Composable
fun HomeScreen(
    bookShelfUiState: BookShelfUiState,
    retryAction: () -> Unit,
    onBookClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    BackHandler(enabled = true) {
        onBackClick()
    }
    AnimatedContent(
        targetState = bookShelfUiState
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
                    val filterGroups = FilterGroups(
                        bookList = targetState.bookList,
                        languageAction = {},
                        authorAction = {}
                    )
                    ChipContainer(filters = filterGroups.languageFilters + filterGroups.authorFilters)
                    ResultScreen(
                        bookList = targetState.bookList,
                        onBookClick = onBookClick
                    )
                }
        }
    }
}