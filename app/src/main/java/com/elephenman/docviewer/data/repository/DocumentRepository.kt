package com.elephenman.docviewer.data.repository

import android.content.Context
import android.net.Uri
import com.elephenman.docviewer.data.model.Document
import com.elephenman.docviewer.data.model.DocumentType
import com.elephenman.docviewer.util.EncodingDetector
import dagger.hilt.android.qualifiers.ApplicationContext
import java.nio.charset.Charset
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DocumentRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun loadDocument(uri: Uri, charset: Charset? = null): Document? {
        val name = uri.lastPathSegment ?: "unknown"
        val type = when {
            name.endsWith(".md", ignoreCase = true) -> DocumentType.MARKDOWN
            name.endsWith(".html", ignoreCase = true) -> DocumentType.HTML
            else -> DocumentType.HTML
        }

        return try {
            val bytes = context.contentResolver.openInputStream(uri)?.use { stream ->
                stream.readBytes()
            } ?: return null

            val detectedCharset = charset ?: EncodingDetector.detect(bytes)
            val content = String(bytes, detectedCharset)

            Document(uri = uri, name = name, content = content, type = type)
        } catch (e: Exception) {
            null
        }
    }

    fun saveDocument(document: Document): Boolean {
        return try {
            context.contentResolver.openOutputStream(document.uri, "wt")?.use { stream ->
                stream.write(document.content.toByteArray())
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
