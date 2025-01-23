package com.example.bookshelf.fake

import com.example.bookshelf.model.Book
import com.example.bookshelf.model.GoogleBooksResponse
import com.example.bookshelf.model.ImageLinks
import com.example.bookshelf.model.VolumeInfo

object FakeDataSource {
    val books = listOf(
        Book(
            id = "1",
            volumeInfo = VolumeInfo(
                title = "The Catcher in the Rye",
                authors = listOf("J.D. Salinger"),
                publishedDate = "1951",
                pageCount = 277,
                imageLinks = ImageLinks(
                    thumbnail = "http://books.google.com/books/content?id=1&printsec=frontcover&img=1&zoom=1&source=gbs_api"
                )
            )
        ),
        Book(
            id = "2",
            volumeInfo = VolumeInfo(
                title = "To Kill a Mockingbird",
                authors = listOf("Harper Lee"),
                publishedDate = "1960",
                pageCount = 281,
                imageLinks = ImageLinks(
                    thumbnail = "http://books.google.com/books/content?id=2&printsec=frontcover&img=1&zoom=1&source=gbs_api"
                )
            )
        ),
        Book(
            id = "3",
            volumeInfo = VolumeInfo(
                title = "1984",
                authors = listOf("George Orwell"),
                publishedDate = "1949",
                pageCount = 328,
                imageLinks = ImageLinks(
                    thumbnail = "http://books.google.com/books/content?id=3&printsec=frontcover&img=1&zoom=1&source=gbs_api"
                )
            )
        )
    )

    val googleBooksResponse = GoogleBooksResponse(items = books)
}