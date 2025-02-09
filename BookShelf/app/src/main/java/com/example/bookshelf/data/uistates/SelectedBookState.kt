package com.example.bookshelf.data.uistates

import com.example.bookshelf.model.ExtendedBook

data class SelectedBookState(
    val book: ExtendedBook? = null,
    val isSaved: Boolean = false
)