package com.example.bookshelf.presentation

import androidx.lifecycle.ViewModel
import com.example.bookshelf.data.SelectedBooksUiState
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ExtendedBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BookSelectionViewModel: ViewModel() {
    private var _selectedBooksUiState = MutableStateFlow(SelectedBooksUiState())
    val selectedBooksUiState: StateFlow<SelectedBooksUiState> = _selectedBooksUiState.asStateFlow()

    fun removeAllBooksFromSelection() {
        _selectedBooksUiState.update { currentState ->
            currentState.copy(
                isSelectionModeOn = false,
                selectedBooks = emptyList()
            )
        }
    }

    fun addAllBooksToSelection(books: List<ExtendedBook>) {
        _selectedBooksUiState.update { currentState ->
            currentState.copy(
                selectedBooks = books
            )
        }
    }

    fun toggleBookInSelection(book: ExtendedBook) {
        _selectedBooksUiState.update { currentState ->
            if (_selectedBooksUiState.value.selectedBooks.contains(book)) {
                currentState.copy(
                    selectedBooks = currentState.selectedBooks - book
                )
            } else {
                currentState.copy(
                    selectedBooks = currentState.selectedBooks + book
                )
            }
        }
        changeSelectionMode()
    }

    private fun changeSelectionMode() {
        _selectedBooksUiState.update { currentState ->
            if (currentState.selectedBooks.isEmpty()) {
                currentState.copy(
                    isSelectionModeOn = false
                )
            } else if (currentState.selectedBooks.size == 1) {
                currentState.copy(
                    isSelectionModeOn = true
                )
            } else {
                currentState
            }
        }
    }
}