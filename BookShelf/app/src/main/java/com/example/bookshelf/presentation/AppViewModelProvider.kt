package com.example.bookshelf.presentation

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.bookshelf.BookShelfApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application: BookShelfApplication = (this[APPLICATION_KEY]) as BookShelfApplication
            val networkBookRepository = application.container.bookRepository
            BookShelfViewModel(networkBookRepository)
        }
        initializer {
            val application: BookShelfApplication = (this[APPLICATION_KEY]) as BookShelfApplication
            val networkBookRepository = application.container.bookRepository
            BookPageViewModel(networkBookRepository)
        }
        initializer {
            MainScreenViewModel()
        }
    }
}