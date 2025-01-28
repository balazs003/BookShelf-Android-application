package com.example.bookshelf.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "books")
@Serializable
data class ExtendedBook(
    @PrimaryKey
    val id: String,
    @Embedded
    val volumeInfo: ExtendedVolumeInfo,
    @Embedded
    val saleInfo: SaleInfo? = null,
    @Embedded
    val accessInfo: AccessInfo? = null
) {
    fun convertToBook(): Book {
        return Book(
            id = id,
            volumeInfo = VolumeInfo(
                title = volumeInfo.title,
                authors = volumeInfo.authors,
                imageLinks = ImageLinks(
                    thumbnail = volumeInfo.imageLinks?.thumbnail
                )
            )
        )
    }
}

@Serializable
data class ExtendedVolumeInfo(
    val title: String? = null,
    val authors: List<String>? = null,
    val publisher: String? = null,
    val publishedDate: String? = null,
    val description: String? = null,
    @Embedded
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
