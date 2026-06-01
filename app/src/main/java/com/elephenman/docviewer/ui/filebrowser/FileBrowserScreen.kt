package com.elephenman.docviewer.ui.filebrowser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elephenman.docviewer.data.model.Document
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileBrowserScreen(
    onFileSelected: (Document) -> Unit,
    viewModel: FileBrowserViewModel = hiltViewModel()
) {
    val currentPath by viewModel.currentPath.collectAsState()
    val files by viewModel.files.collectAsState()
    val selectedDocument by viewModel.selectedDocument.collectAsState()
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(selectedDocument) {
        selectedDocument?.let { doc ->
            onFileSelected(doc)
            viewModel.clearSelectedDocument()
        }
    }

    RequestStoragePermission(onPermissionGranted = { viewModel.loadFiles() }) {
        Scaffold(
            topBar = {
                if (isSearching) {
                    SearchTopBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onClose = {
                            isSearching = false
                            searchQuery = TextFieldValue("")
                            viewModel.clearSearch()
                        }
                    )
                } else {
                    TopAppBar(
                        title = { Text(currentPath.name.ifEmpty { "Storage" }) },
                        navigationIcon = {
                            if (currentPath.parentFile != null) {
                                IconButton(onClick = {
                                    viewModel.navigateUp()
                                    searchQuery = TextFieldValue("")
                                    isSearching = false
                                }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                                }
                            }
                        },
                        actions = {
                            IconButton(onClick = { isSearching = true }) {
                                Icon(Icons.Default.Search, contentDescription = "搜索")
                            }
                        }
                    )
                }
            }
        ) { padding ->
            val displayFiles = if (searchQuery.text.isNotBlank()) {
                files.filter { it.name.contains(searchQuery.text, ignoreCase = true) }
            } else {
                files
            }

            LazyColumn(modifier = Modifier.padding(padding)) {
                items(
                    items = displayFiles,
                    key = { it.path }
                ) { file ->
                    FileItemRow(
                        file = file,
                        onClick = {
                            if (file.isDirectory) {
                                viewModel.navigateTo(File(file.path))
                                searchQuery = TextFieldValue("")
                                isSearching = false
                            } else {
                                viewModel.selectFile(file.path)
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchTopBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit,
    onClose: () -> Unit
) {
    TopAppBar(
        title = {
            TextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("搜索文件...") },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "关闭搜索")
            }
        }
    )
}

@Composable
private fun FileItemRow(file: FileBrowserViewModel.FileItem, onClick: () -> Unit) {
    ListItem(
        modifier = Modifier.clickable(onClick = onClick),
        headlineContent = { Text(file.name) },
        leadingContent = {
            Icon(
                if (file.isDirectory) Icons.Default.Folder else Icons.Default.InsertDriveFile,
                contentDescription = null,
                tint = if (file.isDirectory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    )
}
