package com.example.bookshelf.sharing

import android.app.Activity
import android.content.Intent
import com.example.bookshelf.model.ExtendedBook

object SharingUtils {
    fun shareBook(activity: Activity, book: ExtendedBook?) {
        val builder = StringBuilder()
        builder.appendLine(book?.volumeInfo?.title)
        builder.appendLine("By: " + book?.volumeInfo?.authors?.joinToString(", "))
        builder.append("Link: ")
        builder.appendLine(book?.accessInfo?.webReaderLink)

        val bookInfo = builder.toString()
        createIntent(activity, bookInfo)
    }

    fun shareBooks(activity: Activity, books: List<ExtendedBook?>) {
        val builder = StringBuilder()
        builder.appendLine("My favorite books:\n")
        books.forEachIndexed { index, book ->
            builder.appendLine("${index + 1}. book:")
            builder.appendLine(book?.volumeInfo?.title)
            builder.appendLine("By: " + book?.volumeInfo?.authors?.joinToString(", "))
            builder.append("Link: ")
            builder.appendLine(book?.accessInfo?.webReaderLink)
            if (index < books.size - 1) {
                builder.append("\n")
            }
        }
        val bookInfo = builder.toString()
        createIntent(activity, bookInfo)
    }

    private fun createIntent(activity: Activity, text: String) {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }.also {
            if (it.resolveActivity(activity.packageManager) != null) {
                activity.startActivity(it)
            }
        }
    }
}