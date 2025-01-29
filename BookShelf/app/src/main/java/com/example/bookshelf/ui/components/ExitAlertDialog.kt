package com.example.bookshelf.ui.components

import android.app.Activity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.bookshelf.R

@Composable
fun ExitAlertDialog(activity: Activity, dismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = dismiss,
        title = {
            Text(
                text = stringResource(R.string.close_the_app),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Text(text = stringResource(R.string.do_you_want_to_exit_the_app), fontSize = 16.sp)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dismiss()
                    activity.finish()
                }) {
                Text(
                    text = stringResource(R.string.yes),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    dismiss()
                }) {
                Text(
                    text = stringResource(R.string.no),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
    )
}