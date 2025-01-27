package com.example.bookshelf.data

import com.example.bookshelf.ui.screens.Page
import com.example.bookshelf.ui.screens.Pages

data class MainScreenUiState(
    val selectedPage: Page = Pages.pageList[0],
    val isSearchEnabled: Boolean = true
)