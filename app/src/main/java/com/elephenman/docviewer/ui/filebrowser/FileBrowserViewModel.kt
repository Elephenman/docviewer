package com.elephenman.docviewer.ui.filebrowser

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elephenman.docviewer.data.model.Document
import com.elephenman.docviewer.data.repository.DocumentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileBrowserViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val documentRepository: DocumentRepository
) : ViewModel() {

    private val _currentPath = MutableStateFlow(Environment.getExternalStorageDirectory())
    val currentPath: StateFlow<File> = _currentPath

    private val _files = MutableStateFlow<List<FileItem>>(emptyList())
    val files: StateFlow<List<FileItem>> = _files

    private val _selectedDocument = MutableStateFlow<Document?>(null)
    val selectedDocument: StateFlow<Document?> = _selectedDocument

    init {
        loadFiles()
    }

    fun navigateTo(file: File) {
        if (file.isDirectory) {
            _currentPath.value = file
            loadFiles()
        }
    }

    fun navigateUp() {
        val parent = _currentPath.value.parentFile
        if (parent != null) {
            _currentPath.value = parent
            loadFiles()
        }
    }

    fun selectFile(path: String) {
        viewModelScope.launch {
            val uri = Uri.fromFile(File(path))
            documentRepository.loadDocument(uri)?.let { doc ->
                _selectedDocument.value = doc
            }
        }
    }

    fun clearSelectedDocument() {
        _selectedDocument.value = null
    }

    private fun loadFiles() {
        viewModelScope.launch {
            val current = _currentPath.value
            val items = current.listFiles()?.map { file ->
                FileItem(
                    name = file.name,
                    path = file.absolutePath,
                    isDirectory = file.isDirectory,
                    isSupported = file.extension.lowercase() in listOf("html", "htm", "md")
                )
            }?.sortedWith(compareByDescending<FileItem> { it.isDirectory }.thenBy { it.name.lowercase() }) ?: emptyList()
            _files.value = items
        }
    }

    data class FileItem(
        val name: String,
        val path: String,
        val isDirectory: Boolean,
        val isSupported: Boolean
    )
}
