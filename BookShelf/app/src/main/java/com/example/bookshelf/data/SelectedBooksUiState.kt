package com.example.bookshelf.data

import com.example.bookshelf.model.Book

data class SelectedBooksUiState(
    val isSelectionModeOn: Boolean = false,
    val selectedBooks: List<Book> = emptyList()
)
