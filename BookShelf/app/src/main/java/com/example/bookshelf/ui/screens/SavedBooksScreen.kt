package com.example.bookshelf.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.presentation.AppViewModelProvider
import com.example.bookshelf.presentation.OfflineBookShelfViewModel
import com.example.bookshelf.presentation.BookSelectionViewModel
import com.example.bookshelf.ui.screens.states.SavedPageResultScreen

@Composable
fun SavedBooksScreen(
    offlineViewModel: OfflineBookShelfViewModel,
    onBookClick: (String) -> Unit,
    onBackPressed: () -> Unit
) {
    val bookSelectionViewModel: BookSelectionViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState by bookSelectionViewModel.selectedBooksUiState.collectAsState()

    BackHandler(true) {
        if (uiState.isSelectionModeOn) {
            bookSelectionViewModel.removeAllBooksFromSelection()
        } else {
            onBackPressed()
        }
    }
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        val storedBooks by offlineViewModel.getAllStoredBooks().collectAsState(emptyList())

        if (storedBooks.isEmpty()) {
            Column (
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(R.string.save_books_prompt))
            }
        } else {
            SavedPageResultScreen(
                bookList = storedBooks,
                offlineViewModel = offlineViewModel,
                bookSelectionViewModel = bookSelectionViewModel,
                uiState = uiState,
                onBookClick = onBookClick
            )
        }
    }
}