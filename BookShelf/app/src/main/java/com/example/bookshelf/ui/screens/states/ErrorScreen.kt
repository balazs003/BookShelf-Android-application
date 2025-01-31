package com.example.bookshelf.ui.screens.states

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.bookshelf.R
import com.example.bookshelf.network.rememberConnectivityState

@Composable
fun ErrorScreen(
    errorMessage: String, 
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected by rememberConnectivityState()

    LaunchedEffect(isConnected) {
        if (isConnected){
            retryAction()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 30.dp),
            painter = painterResource(R.drawable.ic_connection_error),
            contentDescription = ""
        )
        Text(
            text = stringResource(R.string.error_screen_text),
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = stringResource(R.string.error_message) + errorMessage,
            style = MaterialTheme.typography.bodyMedium
        )
        Button(
            onClick = { retryAction() }
        ) {
            Text(
                text = stringResource(R.string.retry)
            )
        }
    }
}