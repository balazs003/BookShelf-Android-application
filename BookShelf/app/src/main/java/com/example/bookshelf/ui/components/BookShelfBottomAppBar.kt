package com.example.bookshelf.ui.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.bookshelf.ui.screens.Page
import com.example.bookshelf.ui.screens.Pages

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfBottomAppBar(
    scrollBehavior: BottomAppBarScrollBehavior,
    selectedPage: Page,
    onPageSelect: (Page) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier
    ) {
        NavigationBar(
            containerColor = Color.Transparent
        ) {
            Pages.pageList.forEach { page ->
                NavigationBarItem(
                    selected = page == selectedPage,
                    onClick = {
                        onPageSelect(page)
                    },
                    icon = {
                        Icon(imageVector = page.icon, contentDescription = "")
                    },
                    label = {
                        Text(text = page.name)
                    }
                )
            }
        }
    }
}