package com.example.bookshelf.data

import com.example.bookshelf.ui.screens.Screen

data class MainScreenUiState(
    val selectedScreen: Screen = Screen.HomeScreen,
    val isSearchEnabled: Boolean = true,
    val isSharingEnabled: Boolean = false
)