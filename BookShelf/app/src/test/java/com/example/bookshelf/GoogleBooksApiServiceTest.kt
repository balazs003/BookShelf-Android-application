package com.example.bookshelf

import com.example.bookshelf.fake.FakeApiService
import com.example.bookshelf.network.GoogleBooksApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GoogleBooksApiServiceTest {

    private val service: GoogleBooksApiService = FakeApiService()

    @Test
    fun testResponseFirstItemIdMatch() = runTest {
        val queryString = "flowers"
        val expectedId = "1"
        val actualId = service.getSearchResults(queryString).items?.get(0)?.id
        assertEquals(expectedId, actualId)
    }

    @Test
    fun testThumbnailUrlGetsConvertedToHttps() = runTest {
        val queryString = "flowers"
        val expectedUrl = "https://books.google.com/books/content?id=1&printsec=frontcover&img=1&zoom=1&source=gbs_api"
        val resultBook = service.getSearchResults(queryString).items?.get(0)
        resultBook?.volumeInfo?.imageLinks?.convertThumbnail()
        val actualUrl = resultBook?.volumeInfo?.imageLinks?.thumbnail
        assertEquals(expectedUrl, actualUrl)
    }
}