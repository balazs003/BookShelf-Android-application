package com.example.bookshelf.ui.screens

sealed class Screen(val route: String) {
    data object HomeScreen: Screen(route = "home")
    data object BookScreen: Screen(route = "book/{id}") {
        fun passBookId(bookId: String) = "book/$bookId"
        object Args {
            const val id = "id"
        }
    }
}