package com.example.bookshelf.ui.screens.states

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.data.SelectedBooksUiState
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ExtendedBook
import com.example.bookshelf.presentation.OfflineBookShelfViewModel
import com.example.bookshelf.presentation.BookSelectionViewModel
import com.example.bookshelf.sharing.SharingUtils
import com.example.bookshelf.ui.components.AppAlertDialog
import com.example.bookshelf.ui.components.BookCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResultScreen(
    bookList: List<Book>,
    modifier: Modifier = Modifier,
    isSelectionModeAvailable: Boolean = false,
    offlineViewModel: OfflineBookShelfViewModel? = null,
    bookSelectionViewModel: BookSelectionViewModel? = null,
    uiState: SelectedBooksUiState? = null,
    onBookClick: (String) -> Unit
) {
    val gridCellsMinSize: Dp = 180.dp

    if (isSelectionModeAvailable && uiState != null && bookSelectionViewModel != null) {
        val activity = LocalContext.current as Activity
        val coroutineScope = rememberCoroutineScope()

        var isDialogOpen by rememberSaveable { mutableStateOf(false) }

        Column {
            AnimatedVisibility (visible = uiState.isSelectionModeOn) {
                Row (
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                ) {
                    TextButton(
                        onClick = {
                            if (uiState.selectedBooks.size == bookList.size) {
                                bookSelectionViewModel.removeAllBooksFromSelection()
                            } else {
                                bookSelectionViewModel.addAllBooksToSelection(bookList)
                            }
                        }
                    ) {
                        Checkbox(
                            checked = uiState.selectedBooks.size == bookList.size,
                            onCheckedChange = null
                        )
                        Spacer(Modifier.width(10.dp))
                        Text(
                            text = stringResource(R.string.selected) + uiState.selectedBooks.size.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Row {
                        IconButton(
                            onClick = {
                                shareSelectedBooks(
                                    activity = activity,
                                    viewModel = offlineViewModel,
                                    selectedBooks = uiState.selectedBooks,
                                    coroutineScope = coroutineScope
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.Share,
                                contentDescription = ""
                            )
                        }
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            ),
                            onClick = {
                                isDialogOpen = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.TwoTone.Delete,
                                contentDescription = ""
                            )
                        }
                    }
                }
            }

            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                columns = GridCells.Adaptive(minSize = gridCellsMinSize)
            ) {
                items(items = bookList, key = { book -> book.id }) { book ->
                    val isSelected = book in uiState.selectedBooks

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
                                        if (uiState.isSelectionModeOn) {
                                            bookSelectionViewModel.toggleBookInSelection(book)
                                        } else {
                                            onBookClick(book.id)
                                        }
                                    },
                                    onLongClick = {
                                        if (!uiState.isSelectionModeOn) {
                                            bookSelectionViewModel.toggleBookInSelection(book)
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
                        context.getString(R.string.book_s_deleted, uiState.selectedBooks.size),
                        Toast.LENGTH_SHORT
                    ).show()

                    isDialogOpen = false
                    bookSelectionViewModel.removeAllBooksFromSelection()
                    offlineViewModel?.deleteBooks(
                        bookIds = uiState.selectedBooks.map { book ->
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
}

private fun shareSelectedBooks(
    activity: Activity,
    viewModel: OfflineBookShelfViewModel?,
    selectedBooks: List<Book>,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        val extendedBookList = mutableListOf<ExtendedBook?>()
        selectedBooks.forEach { book ->
            extendedBookList.add(viewModel?.getBookById(book.id))
        }
        if (selectedBooks.size > 1) {
            SharingUtils.shareBooks(
                activity,
                extendedBookList
            )
        } else {
            SharingUtils.shareBook(
                activity,
                extendedBookList[0]
            )
        }
    }
}