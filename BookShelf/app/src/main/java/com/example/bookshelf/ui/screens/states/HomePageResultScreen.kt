package com.example.bookshelf.ui.screens.states

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.components.BookCard

@SuppressLint("ContextCastToActivity")
@Composable
fun HomePageResultScreen(
    bookList: List<Book>,
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit
) {
    val gridCellsMinSize: Dp = 180.dp

    LazyVerticalGrid(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp),
        columns = GridCells.Adaptive(minSize = gridCellsMinSize)
    ) {
        items(items = bookList, key = { book -> book.id }) { book ->
            BookCard(
                bookModel = book,
                modifier = Modifier
                    .height(340.dp)
                    .padding(6.dp),
                onBookClick = onBookClick
            )
        }
    }
}