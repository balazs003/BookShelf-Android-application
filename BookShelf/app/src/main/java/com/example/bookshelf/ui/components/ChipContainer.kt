package com.example.bookshelf.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookshelf.model.Book
import com.example.bookshelf.presentation.Filter

@Composable
fun ChipContainer(filters: List<Filter>) {
    var isOpen: Boolean by rememberSaveable { mutableStateOf(false) }

    val selectedFilters = remember { mutableStateMapOf<Filter, Boolean>() }

    TextButton(
        onClick = {
            isOpen = !isOpen
        },
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = "Filters",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            imageVector = if (isOpen) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = "",
            modifier = Modifier.size(18.dp)
        )
    }

    AnimatedVisibility(visible = isOpen) {
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 14.dp)
        ) {
            filters.forEach { filter ->
                InputChip(
                    selected = selectedFilters[filter] ?: false,
                    onClick = { selectedFilters[filter] = !(selectedFilters[filter] ?: false) },
                    label = { Text(filter.name ?: "default") },
                    leadingIcon = {
                        Icon(filter.icon, contentDescription = "Add")
                    },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}