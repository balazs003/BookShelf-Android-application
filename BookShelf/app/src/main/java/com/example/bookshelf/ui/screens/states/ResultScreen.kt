package com.example.bookshelf.ui.screens.states

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookshelf.R
import com.example.bookshelf.model.Book
import com.example.bookshelf.presentation.OfflineBookShelfViewModel
import com.example.bookshelf.ui.components.AppAlertDialog
import com.example.bookshelf.ui.components.BookCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultScreen(
    bookList: List<Book>,
    modifier: Modifier = Modifier,
    isSelectionModeAvailable: Boolean = false,
    viewModel: OfflineBookShelfViewModel? = null,
    onBookClick: (String) -> Unit
) {
    if (isSelectionModeAvailable) {
        var isDialogOpen by rememberSaveable { mutableStateOf(false) }
        var isSelectionModeActive by rememberSaveable { mutableStateOf(false) }
        var selectedBooks by rememberSaveable { mutableStateOf(listOf<Book>()) }

        Column {
            AnimatedVisibility (visible = isSelectionModeActive) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    TextButton(
                        onClick = {
                            selectedBooks =
                                if (selectedBooks.size == bookList.size) {
                                    isSelectionModeActive = false
                                    listOf()
                                } else {
                                    listOf(*bookList.toTypedArray())
                                }
                        }
                    ) {
                        Checkbox(
                            checked = selectedBooks.size == bookList.size,
                            onCheckedChange = null
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.selected) + selectedBooks.size.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    TextButton(
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        onClick = {
                            isDialogOpen = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = ""
                        )
                        Spacer(Modifier.width(5.dp))
                        Text(
                            text = stringResource(R.string.delete),
                            style = MaterialTheme.typography.bodyLarge
                        )
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
                    val isSelected = book in selectedBooks

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
                                        if (isSelectionModeActive) {
                                            selectedBooks = selectedBooks.toggle(book)
                                            isSelectionModeActive = selectedBooks.isNotEmpty()
                                        } else {
                                            onBookClick(book.id)
                                        }
                                    },
                                    onLongClick = {
                                        if (!isSelectionModeActive) {
                                            isSelectionModeActive = true
                                            selectedBooks += book
                                        }
                                    }
                                )
                        )
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = "",
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
        if (isDialogOpen) {
            val context = LocalContext.current
            AppAlertDialog(
                title = stringResource(R.string.delete_selection),
                text = stringResource(R.string.do_you_really_want_to_delete_the_selected_books),
                onDismiss = {
                    isDialogOpen = false
                },
                onConfirm = {
                    Toast.makeText(
                        context,
                        "${selectedBooks.size} book(s) deleted",
                        Toast.LENGTH_SHORT
                    ).show()
                    isDialogOpen = false
                    isSelectionModeActive = false
                    viewModel?.deleteBooks(
                        bookIds = selectedBooks.map { book ->
                            book.id
                        }
                    )
                }
            )
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

private fun List<Book>.toggle(book: Book): List<Book> {
    return if (contains(book)) this - book else this + book
}