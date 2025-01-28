package com.example.bookshelf.ui.screens.states

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.example.bookshelf.R
import com.example.bookshelf.model.AccessInfo
import com.example.bookshelf.model.ExtendedBook
import com.example.bookshelf.model.ExtendedVolumeInfo
import com.example.bookshelf.model.SaleInfo
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
            Text(
                text = it,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        book.volumeInfo.authors?.let {
            DetailsText(
                title = stringResource(R.string.authors),
                value = it.joinToString(", ")
            )
        }

        book.volumeInfo.language?.let {
            DetailsText(
                title = stringResource(R.string.language),
                value = it
            )
        }

        book.volumeInfo.publisher?.let {
            DetailsText(
                title = stringResource(R.string.publisher),
                value = it
            )
        }

        book.volumeInfo.publishedDate?.let {
            DetailsText(
                title = stringResource(R.string.published_on),
                value = it
            )
        }

        book.volumeInfo.description?.let {
            DetailsText(
                title = stringResource(R.string.description),
                value = it
            )
        }

        book.volumeInfo.pageCount?.let {
            DetailsText(
                title = stringResource(R.string.page_count),
                value = "$it pages"
            )
        }

        book.accessInfo?.webReaderLink?.let {
            DetailsText(
                title = stringResource(R.string.read_online),
                value = it,
                isClickable = true
            )
        }

        book.saleInfo?.buyLink?.let {
            DetailsText(
                title = stringResource(R.string.buy_it),
                value = it,
                isClickable = true
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookPageResultScreenPreview() {
    BookPageResultScreen(
        book = ExtendedBook(
            id = "1",
            volumeInfo = ExtendedVolumeInfo(
                title = "Book Title",
                authors = listOf("Ava Max"),
                publisher = "Nemeth Studios",
                publishedDate = "2022.03.11",
                description = "This is a description text.This is a description text.This is a description text.This is a description text.This is a description text.This is a description text.This is a description text.This is a description text.This is a description text.This is a description text.This is a description text.",
                pageCount = 300
            ),
            accessInfo = AccessInfo(
                webReaderLink = "www.cicakutya.com"
            ),
            saleInfo = SaleInfo(
                buyLink = "www.kutyacica.com"
            )
        )
    )
}