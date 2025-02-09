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
import com.example.bookshelf.ui.screens.Screen
import com.example.bookshelf.ui.screens.ScreenListProvider

@Composable
fun BookShelfBottomAppBar(
    selectedScreen: Screen,
    onScreenSelect: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        modifier = modifier
    ) {
        NavigationBar(
            containerColor = Color.Transparent
        ) {
            ScreenListProvider.bottomNavBarScreens.forEach { screen ->
                NavigationBarItem(
                    selected = screen == selectedScreen,
                    onClick = {
                        onScreenSelect(screen)
                    },
                    icon = {
                        Icon(imageVector = screen.icon, contentDescription = "")
                    },
                    label = {
                        Text(
                            text = screen.route.replaceFirstChar { it.uppercaseChar() },
                            fontWeight = if (screen == selectedScreen) FontWeight.Bold else FontWeight.Normal,
                            color = if (screen == selectedScreen) MaterialTheme.colorScheme.primary else Color.Unspecified
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