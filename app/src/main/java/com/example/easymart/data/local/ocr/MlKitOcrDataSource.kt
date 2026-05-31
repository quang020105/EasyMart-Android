package com.example.easymart.data.local.ocr

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class MlKitOcrDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    suspend fun extractText(uri: Uri): String = withContext(Dispatchers.IO) {
        val image = when (uri.scheme) {
            "http", "https" -> {
                // Load remote image into bitmap for OCR.
                val bitmap = URL(uri.toString()).openStream().use { stream ->
                    BitmapFactory.decodeStream(stream)
                }
                InputImage.fromBitmap(bitmap, 0)
            }
            else -> InputImage.fromFilePath(context, uri)
        }
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        suspendCancellableCoroutine { continuation ->
            recognizer.process(image)
                .addOnSuccessListener { result ->
                    continuation.resume(result.text)
                }
                .addOnFailureListener { error ->
                    continuation.resumeWithException(error)
                }
        }
    }
}
