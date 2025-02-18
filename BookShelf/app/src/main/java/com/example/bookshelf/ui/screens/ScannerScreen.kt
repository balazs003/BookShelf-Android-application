package com.example.bookshelf.ui.screens

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CameraAlt
import androidx.compose.material.icons.twotone.Share
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.bookshelf.R
import com.example.bookshelf.presentation.ScannerViewModel
import com.example.bookshelf.sharing.SharingUtils
import com.google.mlkit.vision.documentscanner.GmsDocumentScanner
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File

@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel,
    onBackPressed: () -> Unit
) {
    BackHandler(true) {
        onBackPressed()
    }

    val uiState by viewModel.scannerUiState.collectAsState()

    val activity = LocalActivity.current
    val context = LocalContext.current

    val options = GmsDocumentScannerOptions.Builder()
        .setScannerMode(SCANNER_MODE_FULL)
        .setGalleryImportAllowed(true)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .build()

    val scanner = GmsDocumentScanning.getClient(options)

    val scannerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = {
            if (it.resultCode == RESULT_OK) {
                val result = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
                viewModel.setImageUris(result)
                viewModel.savePdfToInternalStorage(result, activity, context)
            }
        }
    )
    Column (
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (!uiState.urisAvailable) {
            Button(
                onClick = {
                    onScanClick(
                        scanner = scanner,
                        activity = activity!!,
                        context = context,
                        scannerLauncher = scannerLauncher
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.TwoTone.CameraAlt,
                    contentDescription = null
                )
                Spacer(Modifier.width(10.dp))
                Text(text = stringResource(R.string.scan_pdf))
            }
        } else {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilledTonalButton(
                    onClick = {
                        onScanClick(
                            scanner = scanner,
                            activity = activity!!,
                            context = context,
                            scannerLauncher = scannerLauncher
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.CameraAlt,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(text = stringResource(R.string.scan_another_book))
                }
                Button(
                    onClick = {
                        SharingUtils.sharePdf(activity!!)
                    },
                    enabled = File(activity?.filesDir, context.getString(R.string.pdf_name)).exists()
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.Share,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(text = stringResource(R.string.share_pdf))
                }
            }

            Text(
                text = stringResource(R.string.pages_scanned, uiState.imageUris.size),
                modifier = Modifier.padding(vertical = 3.dp)
            )

            Column (
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.imageUris.forEachIndexed { index, uri ->
                    Text(
                        text = "Page ${index+1}",
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }
}

private fun onScanClick(
    scanner: GmsDocumentScanner,
    activity: Activity,
    context: Context,
    scannerLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>
) {
    scanner.getStartScanIntent(activity)
        .addOnSuccessListener {
            scannerLauncher.launch(
                IntentSenderRequest.Builder(it).build()
            )
        }
        .addOnFailureListener {
            Toast.makeText(
                context,
                it.message,
                Toast.LENGTH_LONG
            ).show()
        }
}