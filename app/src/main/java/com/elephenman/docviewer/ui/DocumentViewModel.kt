package com.elephenman.docviewer.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elephenman.docviewer.data.model.Document
import com.elephenman.docviewer.data.repository.BookshelfRepository
import com.elephenman.docviewer.data.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.nio.charset.Charset

import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor(
    private val documentRepository: DocumentRepository,
    private val bookshelfRepository: BookshelfRepository
) : ViewModel() {

    private val _currentDocument = MutableStateFlow<Document?>(null)
    val currentDocument: StateFlow<Document?> = _currentDocument

    fun setDocument(document: Document) {
        _currentDocument.value = document
        viewModelScope.launch {
            bookshelfRepository.addToBookshelf(document)
        }
    }

    fun loadDocumentFromUri(uri: android.net.Uri, name: String) {
        viewModelScope.launch {
            documentRepository.loadDocument(uri)?.let { doc ->
                _currentDocument.value = doc
                bookshelfRepository.addToBookshelf(doc)
            }
        }
    }

    fun reloadDocumentWithEncoding(document: Document, charset: Charset, onContentUpdated: (String) -> Unit) {
        viewModelScope.launch {
            documentRepository.loadDocument(document.uri, charset)?.let { doc ->
                _currentDocument.value = doc
                onContentUpdated(doc.content)
            }
        }
    }

    fun clearDocument() {
        _currentDocument.value = null
    }

    fun updateDocumentContent(content: String) {
        _currentDocument.value = _currentDocument.value?.copy(content = content, isModified = true)
    }

    fun getCurrentContent(): String? {
        return _currentDocument.value?.content
    }
}
