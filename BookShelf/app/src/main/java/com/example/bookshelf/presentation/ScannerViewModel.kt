package com.example.bookshelf.presentation

import androidx.lifecycle.ViewModel
import com.example.bookshelf.data.uistates.ScannerUiState
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScannerViewModel: ViewModel() {

    private var _scannerUiState = MutableStateFlow(ScannerUiState())
    val scannerUiState = _scannerUiState.asStateFlow()

    fun setImageUris(result: GmsDocumentScanningResult?) {
        _scannerUiState.update { currentState ->
            currentState.copy(
                imageUris = result?.pages?.map { it.imageUri } ?: emptyList(),
                urisAvailable = result != null
            )
        }
    }
}