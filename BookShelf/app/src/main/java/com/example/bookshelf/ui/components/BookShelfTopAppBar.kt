package com.example.bookshelf.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfTopAppBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    enableNavigateBack: Boolean,
    onNavigateBack: () -> Unit,
    enableSearch: Boolean,
    searchInput: String,
    onInputChange: (String) -> Unit,
    onInputClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column (
        modifier = Modifier.fillMaxWidth()
    ) {
        var showSearchField by rememberSaveable { mutableStateOf(false) }

        CenterAlignedTopAppBar(
            scrollBehavior = scrollBehavior,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            navigationIcon = {
                if(enableNavigateBack) {
                    IconButton(
                        onClick = onNavigateBack
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                    }
                }
            },
            actions = {
                if (enableSearch) {
                    IconButton(
                        onClick = { showSearchField = !showSearchField }
                    ) {
                        if (!showSearchField) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = ""
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowUp,
                                contentDescription = ""
                            )
                        }
                    }
                }
            },
            modifier = modifier
        )
        AnimatedVisibility(visible = showSearchField && enableSearch) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                value = searchInput,
                placeholder = {
                    Text(
                        text = stringResource(R.string.input_placeholder)
                    )
                },
                onValueChange = {
                    onInputChange(it)
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = ""
                    )
                },
                trailingIcon = {
                    if (searchInput.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "",
                            modifier = Modifier.clickable {
                                onInputClear()
                            }
                        )
                    }
                }
            )
        }
    }
}