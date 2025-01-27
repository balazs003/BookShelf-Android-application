package com.example.bookshelf.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    data object HomeScreen: Screen(route = "home")
    data object BookScreen: Screen(route = "book/{id}") {
        fun passBookId(bookId: String) = "book/$bookId"
        object Args {
            const val id = "id"
        }
    }
}

object Pages {
    val pageList = listOf(
        Page(name = "Home", icon = Icons.Filled.Home),
        Page(name = "Saved", icon = Icons.Filled.Star)
    )
}

data class Page(
    val name: String,
    val icon: ImageVector
)