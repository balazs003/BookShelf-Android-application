package com.example.bookshelf.ui.screens.states

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.ui.components.BookCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultScreen(
    bookList: List<Book>,
    isSelectionModeAvailable: Boolean = false,
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit
) {
    if (isSelectionModeAvailable) {
        val selectionMode = remember { mutableStateOf(false) }
        val selectedBooks = remember { mutableStateOf(setOf<Book>()) }
        val allSelected = remember { mutableStateOf(false) }

        Column {
            AnimatedVisibility (visible = selectionMode.value) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                    Row {
                        TextButton(
                            onClick = {
                                allSelected.value = !allSelected.value
                                selectedBooks.value = setOf(*bookList.toTypedArray())
                            }
                        ) {
                            Text(
                                text = if (allSelected.value) "Select all" else "Deselect all"
                            )
                        }
                        TextButton(
                            onClick = {
                                selectionMode.value = false
                                selectedBooks.value = emptySet()
                            }
                        ) {
                            Text(stringResource(R.string.clear_selection))
                        }
                    }
                }
            }

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                columns = GridCells.Adaptive(minSize = 190.dp)
            ) {
                items(items = bookList, key = { book -> book.id }) { book ->
                    val isSelected = book in selectedBooks.value

                    Box(
                        modifier = Modifier
                            .height(340.dp)
                            .padding(6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        BookCard(
                            bookModel = book,
                            modifier = Modifier.height(340.dp),
                            onBookClick = {}
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent,
                                    shape = CardDefaults.shape
                                )
                                .border(
                                    width = 4.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CardDefaults.shape
                                )
                                .combinedClickable(
                                    onClick = {
                                        if (selectionMode.value) {
                                            selectedBooks.value = selectedBooks.value.toggle(book)
                                            selectionMode.value = selectedBooks.value.isNotEmpty()
                                        } else {
                                            onBookClick(book.id)
                                        }
                                    },
                                    onLongClick = {
                                        if (!selectionMode.value) {
                                            selectionMode.value = true
                                            selectedBooks.value = setOf(book)
                                        }
                                    }
                                )
                        )

                        //selection checkmark
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "Selected",
                                tint = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            columns = GridCells.Adaptive(minSize = 190.dp)
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
}

private fun Set<Book>.toggle(book: Book): Set<Book> {
    return if (contains(book)) this - book else this + book
}