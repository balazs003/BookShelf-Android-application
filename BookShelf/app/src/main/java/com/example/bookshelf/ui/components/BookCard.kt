package com.example.bookshelf.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.ExtendedBook

@Composable
fun BookCard(
    bookModel: Book,
    modifier: Modifier = Modifier,
    onBookClick: (String) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable { onBookClick(bookModel.id) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        val title = bookModel.volumeInfo.title
        val author: String? = bookModel.volumeInfo.authors?.get(0)

        val trimmingValues = intArrayOf(15, 14)

        AsyncImage(
            model = ImageRequest.Builder(context = LocalContext.current)
                .data(bookModel.volumeInfo.imageLinks?.thumbnail)
                .crossfade(enable = true)
                .build(),
            placeholder = painterResource(R.drawable.loading_img),
            error = painterResource(R.drawable.ic_broken_image),
            contentDescription = stringResource(R.string.img_description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(270.dp)
        )
        Text(
            text = if (title.isNullOrEmpty()) stringResource(R.string.unknown_title)
            else {
                if (title.length >= trimmingValues[0])
                    title.substring(0, trimmingValues[1]) + "..."
                else title
            },
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                horizontal = 6.dp,
                vertical = 2.dp
            )
        )
        Text(
            text = "By: " + if (author.isNullOrEmpty()) stringResource(R.string.unknown_author)
                    else {
                        if(author.length >= trimmingValues[0])
                            author.substring(0, trimmingValues[1]) + "..."
                        else author
                    },
            modifier = Modifier.padding(
                horizontal = 6.dp
            )
        )
    }
}