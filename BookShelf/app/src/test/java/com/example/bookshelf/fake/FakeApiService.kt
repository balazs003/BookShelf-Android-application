package com.example.bookshelf.fake

import com.example.bookshelf.model.GoogleBooksResponse
import com.example.bookshelf.network.GoogleBooksApiService

class FakeApiService: GoogleBooksApiService {
    override suspend fun getSearchResults(queryString: String): GoogleBooksResponse =
        FakeDataSource.googleBooksResponse
}