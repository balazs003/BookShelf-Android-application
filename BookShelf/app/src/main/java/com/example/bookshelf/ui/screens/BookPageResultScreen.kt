package com.example.bookshelf.ui.screens

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bookshelf.R
import com.example.bookshelf.model.ExtendedBook
import com.example.bookshelf.ui.components.DetailsText

@Composable
fun BookPageResultScreen(book: ExtendedBook) {
    val scrollState: ScrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(book.volumeInfo.imageLinks?.large)
                .crossfade(enable = true)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(),
            contentScale = ContentScale.Crop,
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img)
        )

        Spacer(Modifier.height(20.dp))

        book.volumeInfo.title?.let {
            DetailsText(
                value = it,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        book.volumeInfo.authors?.let {
            DetailsText(
                value = stringResource(R.string.authors),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            DetailsText(
                value = it.joinToString(", ")
            )
        }

        book.volumeInfo.publisher?.let {
            DetailsText(
                value = stringResource(R.string.publisher),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            DetailsText(
                value = it
            )
        }

        book.volumeInfo.publishedDate?.let {
            DetailsText(
                value = stringResource(R.string.published_on),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            DetailsText(
                value = it
            )
        }

        book.volumeInfo.description?.let {
            DetailsText(
                value = stringResource(R.string.description),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            DetailsText(
                value = it
            )
        }

        Spacer(Modifier.height(10.dp))

        book.volumeInfo.pageCount?.let {
            DetailsText(
                value = stringResource(R.string.page_count),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            DetailsText(
                value = "$it pages"
            )
        }

        book.accessInfo?.webReaderLink?.let {
            DetailsText(
                value = stringResource(R.string.read_online),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            DetailsText(
                value = it
            )
        }

        book.saleInfo?.buyLink?.let {
            DetailsText(
                value = stringResource(R.string.buy_it),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyLarge
            )
            DetailsText(
                value = it
            )
        }
    }
}