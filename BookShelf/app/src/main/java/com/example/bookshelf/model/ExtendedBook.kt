package com.example.bookshelf.model

import kotlinx.serialization.Serializable

@Serializable
data class ExtendedBook(
    val id: String,
    val volumeInfo: ExtendedVolumeInfo,
    val saleInfo: SaleInfo? = null,
    val accessInfo: AccessInfo? = null
)

@Serializable
data class ExtendedVolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    val imageLinks: ExtendedImageLinks? = null,
    val language: String? = null,
    val pageCount: Int? = null
)

@Serializable
data class ExtendedImageLinks(
    var smallThumbnail: String? = null,
    var thumbnail: String? = null,
    var small: String? = null,
    var medium: String? = null,
    var large: String? = null,
    var extraLarge: String? = null
) {
    fun convertThumbnails() {
        smallThumbnail = smallThumbnail?.replace("http", "https")
        thumbnail = thumbnail?.replace("http", "https")
        small = small?.replace("http", "https")
        medium = medium?.replace("http", "https")
        large = large?.replace("http", "https")
        extraLarge = extraLarge?.replace("http", "https")
    }
}


@Serializable
data class SaleInfo(
    val buyLink: String? = null
)

@Serializable
data class AccessInfo(
    val webReaderLink: String? = null
)
