package com.example.bookshelf.data.uistates

import android.net.Uri

data class ScannerUiState(
    val imageUris: List<Uri> = emptyList(),
    val urisAvailable: Boolean = false
)