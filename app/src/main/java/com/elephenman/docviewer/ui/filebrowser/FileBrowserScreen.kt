package com.elephenman.docviewer.ui.filebrowser

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
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

    LaunchedEffect(selectedDocument) {
        selectedDocument?.let { doc ->
            onFileSelected(doc)
            viewModel.clearSelectedDocument()
        }
    }

    RequestStoragePermission(onPermissionGranted = { viewModel.loadFiles() }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(currentPath.name.ifEmpty { "Storage" }) },
                    navigationIcon = {
                        if (currentPath.parentFile != null) {
                            IconButton(onClick = { viewModel.navigateUp() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    }
                )
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(files) { file ->
                    FileItemRow(
                        file = file,
                        onClick = {
                            if (file.isDirectory) {
                                viewModel.navigateTo(File(file.path))
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
