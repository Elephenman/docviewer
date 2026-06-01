package com.elephenman.docviewer.ui

import androidx.lifecycle.ViewModel
import com.elephenman.docviewer.data.model.Document
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DocumentViewModel @Inject constructor() : ViewModel() {

    private val _currentDocument = MutableStateFlow<Document?>(null)
    val currentDocument: StateFlow<Document?> = _currentDocument

    fun setDocument(document: Document) {
        _currentDocument.value = document
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
