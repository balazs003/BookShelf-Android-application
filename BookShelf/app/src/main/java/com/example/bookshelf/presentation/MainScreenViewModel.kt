package com.example.bookshelf.presentation

import androidx.lifecycle.ViewModel
import com.example.bookshelf.data.MainScreenUiState
import com.example.bookshelf.ui.screens.Page
import com.example.bookshelf.ui.screens.Pages
import com.example.bookshelf.ui.screens.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class MainScreenViewModel: ViewModel() {
    private var _mainScreenUiState = MutableStateFlow(MainScreenUiState())
    val mainScreenUiState: StateFlow<MainScreenUiState> = _mainScreenUiState.asStateFlow()

    fun changeSelectedPage(newPage: Page) {
        _mainScreenUiState.update { currentState ->
            currentState.copy(
                selectedPage = newPage,
                isSearchEnabled = newPage.name == Screen.HomeScreen.route,
                isSharingEnabled = Screen.BookScreen.route.startsWith(newPage.name.lowercase(Locale.getDefault()))
            )
        }
    }

    fun canNavigateBack(route: String?): Boolean {
        val routes: List<String> = Pages.pageList.map { it.name }
        return !routes.contains(route)
    }
}