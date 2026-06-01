package com.elephenman.docviewer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.elephenman.docviewer.data.model.Document
import com.elephenman.docviewer.ui.editor.EditorScreen
import com.elephenman.docviewer.ui.viewer.ViewerScreen

enum class ViewMode {
    READ,
    EDIT
}

@Composable
fun DocumentScreen(document: Document) {
    var currentMode by remember { mutableStateOf(ViewMode.READ) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.MenuBook, contentDescription = "阅读") },
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
                ViewMode.READ -> ViewerScreen(document = document)
                ViewMode.EDIT -> EditorScreen(document = document)
            }
        }
    }
}
