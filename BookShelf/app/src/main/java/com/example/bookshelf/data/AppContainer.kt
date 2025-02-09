package com.example.bookshelf.data

import android.content.Context
import com.example.bookshelf.data.database.BookDatabase
import com.example.bookshelf.data.repositories.DbOfflineBookRepository
import com.example.bookshelf.data.repositories.NetworkOnlineBookRepository
import com.example.bookshelf.data.repositories.OfflineBookRepository
import com.example.bookshelf.data.repositories.OnlineBookRepository
import com.example.bookshelf.network.GoogleBooksApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val onlineBookRepository: OnlineBookRepository
    val offlineBookRepository: OfflineBookRepository
}

class DefaultAppContainer(
    private val context: Context
): AppContainer {
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

    override val offlineBookRepository: OfflineBookRepository by lazy {
        DbOfflineBookRepository(BookDatabase.getBookDatabase(context).getDao())
    }
}