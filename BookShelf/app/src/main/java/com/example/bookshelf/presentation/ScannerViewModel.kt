package com.example.bookshelf.presentation

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.bookshelf.R
import com.example.bookshelf.data.uistates.ScannerUiState
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.File
import java.io.FileOutputStream

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

    fun savePdfToInternalStorage(result: GmsDocumentScanningResult?, activity: Activity?, context: Context) {
        result?.pdf?.let { pdf ->
            val stream = FileOutputStream(
                File(activity?.filesDir, context.getString(R.string.pdf_name))
            )
            activity?.contentResolver?.openInputStream(pdf.uri)?.use {
                it.copyTo(stream)
            }
        }
    }
}