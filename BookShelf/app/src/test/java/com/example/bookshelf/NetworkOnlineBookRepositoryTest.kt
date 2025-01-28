package com.example.bookshelf

import com.example.bookshelf.data.NetworkOnlineBookRepository
import com.example.bookshelf.fake.FakeApiService
import com.example.bookshelf.fake.FakeDataSource
import com.example.bookshelf.network.GoogleBooksApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkOnlineBookRepositoryTest {

    private val service: GoogleBooksApiService = FakeApiService()
    private val networkRepository =
        NetworkOnlineBookRepository(service)

    @Test
    fun testNetworkBookRepositoryResponseIsValid() = runTest {
        val expectedBooks = FakeDataSource.books.map { book ->
            book.copy(
                volumeInfo = book.volumeInfo.copy(
                    imageLinks = book.volumeInfo.imageLinks?.thumbnail?.replace("http", "https")?.let {
                        book.volumeInfo.imageLinks?.copy(
                            thumbnail = it
                        )
                    }
                )
            )
        }

        //changing query string does not have an effect on the result now
        val actualBooks = networkRepository.getSearchResults("flower")

        assertEquals(expectedBooks, actualBooks)
    }

    @Test
    fun testNetworkRepositoryQueryStringConverter() {
        val queryString = "   jazz music  "
        val expectedConvertedQueryString = "jazz+music"
        val actualConvertedQueryString = networkRepository
            .convertQueryString(queryString)

        assertEquals(expectedConvertedQueryString, actualConvertedQueryString)
    }
}