package com.example.bookshelf.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.bookshelf.model.Book

data class Filter(
    val name: String?,
    val icon: ImageVector,
    val enabled: Boolean,
    val action: (String) -> Unit
)

class FilterGroups(
    bookList: List<Book>,
    languageAction: (String) -> Unit,
    authorAction: (String) -> Unit
) {
    val languageFilters = bookList.map { book ->
        Filter(
            name = book.volumeInfo.language,
            icon = Icons.Default.Place,
            enabled = false,
            action = languageAction
        )
    }.distinctBy { it.name }

    val authorFilters = bookList.map { book ->
        Filter(
            name = book.volumeInfo.authors?.get(0),
            icon = Icons.Default.Person,
            enabled = false,
            action = authorAction
        )
    }.distinctBy { it.name }
}