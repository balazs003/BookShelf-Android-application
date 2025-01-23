package com.example.bookshelf.network

import com.example.bookshelf.model.ExtendedBook
import com.example.bookshelf.model.GoogleBooksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GoogleBooksApiService {
    @GET("volumes")
    suspend fun getSearchResults(@Query("q") queryString: String): GoogleBooksResponse

    @GET("volumes/{id}")
    suspend fun getBookDetails(@Path("id") bookId: String): ExtendedBook
}