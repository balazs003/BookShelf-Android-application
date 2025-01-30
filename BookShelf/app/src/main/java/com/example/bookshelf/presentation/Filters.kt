package com.example.bookshelf.presentation

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bookshelf.model.Book

data class Filter(
    val name: String,
    val icon: ImageVector,
    var selected: Boolean
)

class FilterGroups(
    private val fullBookList: List<Book>,  // Store the original book list
    private val onFiltersChanged: (List<Book>) -> Unit // Callback to update UI
) {
    private val languageFilters = fullBookList
        .filter { !it.volumeInfo.language.isNullOrEmpty() }
        .map { book -> Filter(name = book.volumeInfo.language!!, icon = Icons.Default.Place, selected = false) }
        .distinctBy { it.name }

    private val authorFilters = fullBookList
        .filter { !it.volumeInfo.authors.isNullOrEmpty() }
        .map { book -> Filter(name = book.volumeInfo.authors?.get(0) ?: "", icon = Icons.Default.Person, selected = false) }
        .distinctBy { it.name }

    val filters = languageFilters + authorFilters

    val selectedLanguages = mutableStateListOf<String>()
    val selectedAuthors = mutableStateListOf<String>()

    fun toggleFilter(name: String) {
        // Toggle language filters
        if (languageFilters.any { it.name == name }) {
            if (selectedLanguages.contains(name)) selectedLanguages.remove(name)
            else selectedLanguages.add(name)
        }

        // Toggle author filters
        if (authorFilters.any { it.name == name }) {
            if (selectedAuthors.contains(name)) selectedAuthors.remove(name)
            else selectedAuthors.add(name)
        }

        // Update filtered book list
        applyFilters()
    }

    private fun applyFilters() {
        val filteredList = fullBookList.filter { book ->
            val matchesLanguage = selectedLanguages.isEmpty() || selectedLanguages.contains(book.volumeInfo.language)
            val matchesAuthor = selectedAuthors.isEmpty() || book.volumeInfo.authors?.any { it in selectedAuthors } == true
            matchesLanguage && matchesAuthor
        }
        onFiltersChanged(filteredList)
    }
}