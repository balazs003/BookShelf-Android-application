package com.example.bookshelf.data

import com.example.bookshelf.model.ExtendedBook

data class SelectedBooksUiState(
    val isSelectionModeOn: Boolean = false,
    val selectedBooks: List<ExtendedBook> = emptyList()
)
