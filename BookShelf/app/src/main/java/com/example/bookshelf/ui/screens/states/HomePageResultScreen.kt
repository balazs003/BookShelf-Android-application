package com.example.bookshelf.ui.screens.states

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.components.BookCard

@Composable
fun HomePageResultScreen(
    bookList: List<Book>,
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 200.dp)
    ) {
        items(items = bookList, key = { book -> book.id }) { book ->
            BookCard(
                bookModel = book,
                modifier = Modifier.height(340.dp),
                onBookClick = onBookClick
            )
        }
    }
}