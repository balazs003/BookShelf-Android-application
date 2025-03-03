package com.example.bookshelf.sharing

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.bookshelf.R
import com.example.bookshelf.model.ExtendedBook
import java.io.File

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

    fun sharePdf(activity: Activity) {
        val pdfFile = File(activity.filesDir, activity.getString(R.string.pdf_name))
        if (!pdfFile.exists()) {
            Toast.makeText(activity, "No PDF to share", Toast.LENGTH_SHORT).show()
            return
        }

        val uri: Uri = FileProvider.getUriForFile(
            activity,
            "${activity.packageName}.provider",
            pdfFile
        )

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        activity.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
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