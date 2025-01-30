package com.example.bookshelf.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bookshelf.model.Book
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Filter(
    val name: String,
    var icon: ImageVector,
    var selected: MutableState<Boolean>
)

class FilterGroups(
    private val fullBookList: List<Book>,
    private val onFiltersChanged: (List<Book>) -> Unit
) {
    private val languageFilters = fullBookList
        .filter { !it.volumeInfo.language.isNullOrEmpty() }
        .map { book -> Filter(
            name = book.volumeInfo.language!!,
            icon = Icons.Default.Place,
            selected = mutableStateOf(false)
        ) }
        .distinctBy { it.name }

    private val authorFilters = fullBookList
        .filter { !it.volumeInfo.authors.isNullOrEmpty() }
        .map { book -> Filter(
            name = book.volumeInfo.authors?.get(0) ?: "",
            icon = Icons.Default.Person,
            selected = mutableStateOf(false)
        ) }
        .distinctBy { it.name }

    private var _filters: MutableStateFlow<List<Filter>> = MutableStateFlow(languageFilters + authorFilters)
    val filters: StateFlow<List<Filter>> = _filters.asStateFlow()

    private val selectedLanguages = mutableStateListOf<String>()
    private val selectedAuthors = mutableStateListOf<String>()

    var isFilterSelected by mutableStateOf(selectedLanguages.isNotEmpty() || selectedAuthors.isNotEmpty())

    init {
        _filters.value = languageFilters + authorFilters
    }

    fun deleteFilters() {
        _filters.value = emptyList()
    }

    fun resetFilters() {
        _filters.value.forEach { filter ->
            if (filter.selected.value) {
                toggleFilter(filter)
            }
        }
    }

    fun toggleFilter(filter: Filter) {
        filter.selected.value = !filter.selected.value

        if (languageFilters.any { it.name == filter.name }) {
            if (selectedLanguages.contains(filter.name)) selectedLanguages.remove(filter.name)
            else selectedLanguages.add(filter.name)
        }

        if (authorFilters.any { it.name == filter.name }) {
            if (selectedAuthors.contains(filter.name)) selectedAuthors.remove(filter.name)
            else selectedAuthors.add(filter.name)
        }

        applyFilters()
    }

    private fun applyFilters() {
        val filteredList = fullBookList.filter { book ->
            val matchesLanguage = selectedLanguages.isEmpty() || selectedLanguages.contains(book.volumeInfo.language)
            val matchesAuthor = selectedAuthors.isEmpty() || book.volumeInfo.authors?.any { it in selectedAuthors } == true
            matchesLanguage && matchesAuthor
        }
        onFiltersChanged(filteredList)

        isFilterSelected = selectedLanguages.isNotEmpty() || selectedAuthors.isNotEmpty()
    }
}