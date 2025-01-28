package com.example.bookshelf.data

import androidx.annotation.VisibleForTesting
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ExtendedBook
import com.example.bookshelf.network.GoogleBooksApiService

interface OnlineBookRepository {
    suspend fun getSearchResults(queryString: String): List<Book>
    suspend fun getBookDetails(bookId: String): ExtendedBook
}

class NetworkOnlineBookRepository(
    private val apiService: GoogleBooksApiService
): OnlineBookRepository {
    /**
     * Returns the list of books with formatted image url-s
     **/
    override suspend fun getSearchResults(queryString: String): List<Book> {
        val convertedQueryString = convertQueryString(queryString)

        val bookList = apiService.getSearchResults(convertedQueryString).items

        bookList?.let {
            for (book in bookList)
                book.volumeInfo.imageLinks?.convertThumbnail()
            return bookList
        }

        return emptyList()
    }

    override suspend fun getBookDetails(bookId: String): ExtendedBook {
        val book = apiService.getBookDetails(bookId)
        book.volumeInfo.imageLinks?.convertThumbnails()
        return book
    }

    @VisibleForTesting
    internal fun convertQueryString(queryString: String): String =
        queryString.trim().replace(" ", "+")
}