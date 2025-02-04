package com.example.bookshelf.ui.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.bookshelf.ui.screens.Page
import com.example.bookshelf.ui.screens.Pages

@Composable
fun BookShelfBottomAppBar(
    selectedPage: Page,
    onPageSelect: (Page) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
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
                        Text(
                            text = page.name.replaceFirstChar { it.uppercaseChar() },
                            fontWeight = if (page == selectedPage) FontWeight.Bold else FontWeight.Normal,
                            color = if (page == selectedPage) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            }
        }
    }
}