package com.elephenman.docviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.elephenman.docviewer.data.model.Document
import com.elephenman.docviewer.ui.editor.EditorScreen
import com.elephenman.docviewer.ui.viewer.ViewerScreen

enum class ViewMode {
    READ,
    EDIT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentScreen(
    document: Document,
    documentViewModel: DocumentViewModel = hiltViewModel()
) {
    var currentMode by remember { mutableStateOf(ViewMode.READ) }
    var documentContent by remember { mutableStateOf(document.content) }
    var showEncodingMenu by remember { mutableStateOf(false) }
    var currentEncoding by remember { mutableStateOf("UTF-8") }

    val updatedDocument = remember(documentContent) {
        document.copy(content = documentContent)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(document.name) },
                actions = {
                    IconButton(onClick = { showEncodingMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多选项")
                    }
                    DropdownMenu(
                        expanded = showEncodingMenu,
                        onDismissRequest = { showEncodingMenu = false }
                    ) {
                        val encodings = listOf("UTF-8", "GBK", "GB2312", "BIG5", "ISO-8859-1")
                        encodings.forEach { encoding ->
                            DropdownMenuItem(
                                text = { Text("编码: $encoding") },
                                onClick = {
                                    currentEncoding = encoding
                                    showEncodingMenu = false
                                    reloadDocumentWithEncoding(document, encoding, documentViewModel) { newContent ->
                                        documentContent = newContent
                                    }
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "阅读") },
                    label = { Text("阅读") },
                    selected = currentMode == ViewMode.READ,
                    onClick = { currentMode = ViewMode.READ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Edit, contentDescription = "编辑") },
                    label = { Text("编辑") },
                    selected = currentMode == ViewMode.EDIT,
                    onClick = { currentMode = ViewMode.EDIT }
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (currentMode) {
                ViewMode.READ -> ViewerScreen(document = updatedDocument)
                ViewMode.EDIT -> EditorScreen(
                    document = updatedDocument,
                    onContentSaved = { newContent ->
                        documentContent = newContent
                    }
                )
            }
        }
    }
}

private fun reloadDocumentWithEncoding(
    document: Document,
    encoding: String,
    documentViewModel: DocumentViewModel,
    onContentUpdated: (String) -> Unit
) {
    try {
        val charset = java.nio.charset.Charset.forName(encoding)
        documentViewModel.reloadDocumentWithEncoding(document, charset) { newContent ->
            onContentUpdated(newContent)
        }
    } catch (_: Exception) {
    }
}
