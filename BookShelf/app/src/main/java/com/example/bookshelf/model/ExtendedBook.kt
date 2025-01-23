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
    val pageCount: Int? = null,
    val printedPageCount: Int? = null,
    val categories: List<String>? = null,
    val maturityRating: String? = null,
    val imageLinks: ExtendedImageLinks? = null,
    val language: String? = null,
    val previewLink: String? = null,
    val infoLink: String? = null,
    val canonicalVolumeLink: String? = null
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
    val country: String? = null,
    val saleability: String? = null,
    val isEbook: Boolean? = null,
    val listPrice: Price? = null,
    val retailPrice: Price? = null,
    val buyLink: String? = null
)

@Serializable
data class Price(
    val amount: Double? = null,
    val currencyCode: String? = null
)

@Serializable
data class AccessInfo(
    val epub: Format? = null,
    val pdf: Format? = null,
    val webReaderLink: String? = null
)

@Serializable
data class Format(
    val isAvailable: Boolean? = null,
    val acsTokenLink: String? = null
)
