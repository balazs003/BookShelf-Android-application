package com.example.bookshelf.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object HomeScreen: Screen(route = "home")
    data object SavedScreen: Screen(route = "saved")
    data object BookScreen: Screen(route = "book/{id}") {
        fun passBookId(bookId: String) = "book/$bookId"
        object Args {
            const val id = "id"
        }
    }
}

object Pages {
    val homePage = Page(name = Screen.HomeScreen.route, icon = Icons.Filled.Home)
    val savedPage = Page(name = Screen.SavedScreen.route, icon = Icons.Filled.Star)
    val pageList = listOf(
        homePage,
        savedPage
    )
}

data class Page(
    val name: String,
    val icon: ImageVector = Icons.Default.Home
)