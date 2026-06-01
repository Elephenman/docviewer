package com.elephenman.docviewer.ui.viewer

import androidx.lifecycle.ViewModel
import com.elephenman.docviewer.data.model.Document
import com.elephenman.docviewer.data.model.DocumentType
import com.elephenman.docviewer.util.MarkdownConverter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ViewerViewModel @Inject constructor() : ViewModel() {

    private val _htmlContent = MutableStateFlow("")
    val htmlContent: StateFlow<String> = _htmlContent

    fun loadDocument(document: Document) {
        _htmlContent.value = when (document.type) {
            DocumentType.MARKDOWN -> MarkdownConverter.toHtml(document.content)
            DocumentType.HTML -> document.content
        }
    }
}
