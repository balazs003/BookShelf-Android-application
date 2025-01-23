package com.example.bookshelf.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookShelfBottomAppBar(
    searchInput: String,
    onInputChange: (String) -> Unit,
    onInputClear: () -> Unit,
    scrollBehavior: BottomAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
                if(searchInput.isNotEmpty()) {
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