package com.elephenman.docviewer.data.model

import android.net.Uri

enum class DocumentType {
    HTML,
    MARKDOWN
}

data class Document(
    val uri: Uri,
    val name: String,
    val content: String = "",
    val isModified: Boolean = false,
    val type: DocumentType
)
