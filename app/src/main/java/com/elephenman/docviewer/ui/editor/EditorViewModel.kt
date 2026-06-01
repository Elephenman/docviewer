package com.elephenman.docviewer.ui.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elephenman.docviewer.data.model.Document
import com.elephenman.docviewer.data.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class EditorViewModel @Inject constructor(
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    private val _isModified = MutableStateFlow(false)
    val isModified: StateFlow<Boolean> = _isModified

    private var currentDocument: Document? = null
    private var onContentSaved: ((String) -> Unit)? = null

    private val saveFlow = _content
        .debounce(500)
        .filter { it.isNotEmpty() && currentDocument != null }

    init {
        viewModelScope.launch {
            saveFlow.collect { content ->
                saveDocument(content)
            }
        }
    }

    fun setOnContentSavedListener(listener: (String) -> Unit) {
        onContentSaved = listener
    }

    fun loadDocument(document: Document) {
        currentDocument = document
        _content.value = document.content
        _isModified.value = false
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
        _isModified.value = true
    }

    private fun saveDocument(content: String) {
        currentDocument?.let { doc ->
            val updated = doc.copy(content = content, isModified = false)
            val success = documentRepository.saveDocument(updated)
            if (success) {
                onContentSaved?.invoke(content)
            } else {
                _isModified.value = true
            }
        }
    }

    fun forceSave() {
        saveDocument(_content.value)
        _isModified.value = false
    }
}
