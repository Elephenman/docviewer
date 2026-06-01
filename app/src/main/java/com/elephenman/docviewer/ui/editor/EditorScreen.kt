package com.elephenman.docviewer.ui.editor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.elephenman.docviewer.data.model.Document

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    document: Document,
    viewModel: EditorViewModel = hiltViewModel()
) {
    val content by viewModel.content.collectAsState()
    val isModified by viewModel.isModified.collectAsState()

    LaunchedEffect(document) {
        viewModel.loadDocument(document)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.forceSave()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(document.name + if (isModified) " *" else "") }
            )
        }
    ) { padding ->
        BasicTextField(
            value = content,
            onValueChange = { viewModel.updateContent(it) },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            textStyle = TextStyle(
                fontSize = 14.sp,
                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                lineHeight = 20.sp
            )
        )
    }
}
