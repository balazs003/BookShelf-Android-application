package com.example.bookshelf.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.presentation.OnlineBookShelfViewModel

@Composable
fun ChipContainer(viewModel: OnlineBookShelfViewModel) {
    var isOpen: Boolean by rememberSaveable { mutableStateOf(false) }

    TextButton(
        onClick = {
            isOpen = !isOpen
        },
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Text(
            text = stringResource(R.string.filters),
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
            viewModel.filterGroups.filters.forEach { filter ->
                FilterChip(
                    selected = viewModel.filterGroups.selectedLanguages.contains(filter.name) || viewModel.filterGroups.selectedAuthors.contains(filter.name),
                    onClick = { viewModel.toggleFilter(filter.name) },
                    label = { Text(filter.name) },
                    leadingIcon = { Icon(filter.icon, contentDescription = null) },
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
        }
    }
}
