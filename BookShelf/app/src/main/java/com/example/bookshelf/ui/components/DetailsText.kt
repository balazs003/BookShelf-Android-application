package com.example.bookshelf.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun DetailsText(
    title: String,
    value: String,
    isClickable: Boolean = false
) {
    val context = LocalContext.current
    Text(
        text = title,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 4.dp),
        color = MaterialTheme.colorScheme.primary
    )
    Text(
        text = value,
        fontWeight = FontWeight.Normal,
        style = TextStyle(
            textDecoration = if(isClickable) TextDecoration.Underline else TextDecoration.None
        ),
        color = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier
            .padding(bottom = 16.dp)
            .clickable(enabled = isClickable) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(value))
                context.startActivity(intent)
            }
    )
}