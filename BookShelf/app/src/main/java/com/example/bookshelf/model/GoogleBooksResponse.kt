package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class GoogleBooksResponse(
    val items: List<Book>? = null
)

@Serializable
data class Book(
    val id: String,
    val volumeInfo: VolumeInfo
)

@Serializable
data class VolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val imageLinks: ImageLinks? = null,
    val language: String? = null,
    val pageCount: Int? = null
)

@Serializable
data class ImageLinks(
    var thumbnail: String? = null
) {
    fun convertThumbnail() {
        thumbnail = thumbnail?.replace("http", "https")
    }
}