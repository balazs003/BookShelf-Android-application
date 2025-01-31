package com.example.bookshelf.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookshelf.data.OfflineBookRepository
import com.example.bookshelf.data.OnlineBookRepository
import com.example.bookshelf.data.SelectedBookState
import com.example.bookshelf.model.ExtendedBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface BookPageUiState {
    data class Success(val book: ExtendedBook): BookPageUiState
    data class Error(val errorMessage: String): BookPageUiState
    data object Loading: BookPageUiState
}

class BookPageViewModel(
    private val onlineBookRepository: OnlineBookRepository,
    private val offlineBookRepository: OfflineBookRepository
): ViewModel() {
    var bookPageUiState: BookPageUiState by mutableStateOf(BookPageUiState.Loading)
    private var _selectedBookState = MutableStateFlow(SelectedBookState())
    val selectedBookState: StateFlow<SelectedBookState> = _selectedBookState.asStateFlow()

    //this works as a cache for saved book id-s so that the content of the FAB can load faster
    private var inMemorySavedBookIds: MutableList<String> = mutableListOf("")

    init {
        viewModelScope.launch {
            val storedBooks = offlineBookRepository.getAllStoredBooksStream().first()
            inMemorySavedBookIds.addAll(
                storedBooks.map { book ->
                    book.id
                }
            )
        }
    }

    fun getBookDetailsFromNetwork(bookId: String) {
        viewModelScope.launch {
            bookPageUiState = try {
                val book = onlineBookRepository.getBookDetails(bookId)
                val saved = inMemorySavedBookIds.contains(bookId)
                updateSelectedBookState(book, saved)
                BookPageUiState.Success(book)
            } catch (e: IOException) {
                updateSelectedBookState(null, false)
                BookPageUiState.Error(e.message ?: "IOException")
            } catch (e: HttpException) {
                updateSelectedBookState(null, false)
                BookPageUiState.Error(e.message ?: "HttpException")
            }
        }
    }

    fun getBookDetailsFromStorage(bookId: String) {
        viewModelScope.launch {
            val book = offlineBookRepository.getStoredBookById(bookId)
            updateSelectedBookState(book, isSaved = true)
            bookPageUiState = BookPageUiState.Success(book!!)
        }
    }

    fun saveBook(book: ExtendedBook) {
        viewModelScope.launch {
            offlineBookRepository.saveBook(book)
            updateSelectedBookState(book, isSaved = true)
        }
        inMemorySavedBookIds.add(book.id)
    }

    fun deleteBook(book: ExtendedBook) {
        viewModelScope.launch {
            offlineBookRepository.deleteBook(book)
            updateSelectedBookState(book, isSaved = false)
        }
        inMemorySavedBookIds.remove(book.id)
    }

    private fun updateSelectedBookState(
        book: ExtendedBook? = _selectedBookState.value.book,
        isSaved: Boolean = _selectedBookState.value.isSaved
    ) {
        _selectedBookState.update { currentState ->
            currentState.copy(
                book = book,
                isSaved = isSaved
            )
        }
    }
}