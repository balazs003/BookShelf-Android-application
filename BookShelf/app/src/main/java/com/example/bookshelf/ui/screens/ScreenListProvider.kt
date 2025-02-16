package com.example.bookshelf.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bookshelf.ui.screens.Screen.SavedScreen

sealed class Screen(
    val route: String,
    val title: String,
    val icon: ImageVector = Icons.Default.Home
) {
    data object HomeScreen: Screen(route = "home", title = "Home", icon = Icons.Filled.Home)
    data object SavedScreen: Screen(route = "saved", title = "Saved", icon = Icons.Filled.Star)
    data object BookScreen: Screen(route = "book/{id}", title = "Book details", icon = Icons.Default.Home) {
        fun passBookId(bookId: String) = "book/$bookId"
        object Args {
            const val id = "id"
        }
    }
    data object ScannerScreen: Screen(route = "scan", title = "Scan books", icon = Icons.Default.CameraAlt)
}

object ScreenListProvider {
    val allScreens = listOf(Screen.HomeScreen, Screen.SavedScreen, Screen.BookScreen, Screen.ScannerScreen)
    val bottomNavBarScreens = listOf(Screen.HomeScreen, SavedScreen, Screen.ScannerScreen)
}