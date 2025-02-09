package com.example.bookshelf.presentation

import androidx.lifecycle.ViewModel
import com.example.bookshelf.data.MainScreenUiState
import com.example.bookshelf.ui.screens.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainScreenViewModel: ViewModel() {
    private var _mainScreenUiState = MutableStateFlow(MainScreenUiState())
    val mainScreenUiState: StateFlow<MainScreenUiState> = _mainScreenUiState.asStateFlow()

    fun changeSelectedScreen(newScreen: Screen) {
        _mainScreenUiState.update { currentState ->
            currentState.copy(
                selectedScreen = newScreen,
                isSearchEnabled = newScreen.route == Screen.HomeScreen.route,
                isSharingEnabled = newScreen.route == Screen.BookScreen.route
            )
        }
    }

    fun canNavigateBack(route: String?): Boolean {
        return route?.startsWith(Screen.BookScreen.route) ?: false
    }
}