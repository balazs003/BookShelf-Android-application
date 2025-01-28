package com.example.bookshelf.data

import com.example.bookshelf.network.GoogleBooksApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val onlineBookRepository: OnlineBookRepository
}

class DefaultAppContainer: AppContainer {
    private val baseUrl: String = "https://www.googleapis.com/books/v1/"

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: GoogleBooksApiService by lazy {
        retrofit.create(GoogleBooksApiService::class.java)
    }

    override val onlineBookRepository: OnlineBookRepository by lazy {
        NetworkOnlineBookRepository(retrofitService)
    }
}