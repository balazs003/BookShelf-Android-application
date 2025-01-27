package com.example.bookshelf.ui.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfBottomAppBar(
    scrollBehavior: BottomAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier
    ) {
        Text(text = "placeholder")
    }
}