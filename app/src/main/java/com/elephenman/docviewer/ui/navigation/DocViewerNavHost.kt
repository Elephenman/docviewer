package com.elephenman.docviewer.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.elephenman.docviewer.ui.DocumentScreen
import com.elephenman.docviewer.ui.DocumentViewModel
import com.elephenman.docviewer.ui.bookshelf.BookshelfScreen
import com.elephenman.docviewer.ui.filebrowser.FileBrowserScreen
import androidx.hilt.navigation.compose.hiltViewModel
import android.net.Uri

sealed class Screen(val route: String) {
    data object Bookshelf : Screen("bookshelf")
    data object FileBrowser : Screen("file_browser")
    data object Document : Screen("document")
}

@Composable
fun DocViewerNavHost(
    navController: NavHostController = rememberNavController(),
    externalUri: Uri? = null
) {
    val documentViewModel: DocumentViewModel = hiltViewModel()
    val currentDocument by documentViewModel.currentDocument.collectAsState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(externalUri) {
        externalUri?.let { uri ->
            val name = uri.lastPathSegment ?: "unknown"
            documentViewModel.loadDocumentFromUri(uri, name)
            navController.navigate(Screen.Document.route) {
                popUpTo(Screen.Bookshelf.route) { inclusive = false }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Document.route) {
                NavigationBar {
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "书架") },
                        label = { Text("书架") },
                        selected = currentRoute == Screen.Bookshelf.route,
                        onClick = {
                            navController.navigate(Screen.Bookshelf.route) {
                                popUpTo(Screen.Bookshelf.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.FolderOpen, contentDescription = "文件") },
                        label = { Text("文件") },
                        selected = currentRoute == Screen.FileBrowser.route,
                        onClick = {
                            navController.navigate(Screen.FileBrowser.route) {
                                popUpTo(Screen.FileBrowser.route) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Bookshelf.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Bookshelf.route) {
                BookshelfScreen(
                    onDocumentSelected = { uriString, name ->
                        val uri = Uri.parse(uriString)
                        documentViewModel.loadDocumentFromUri(uri, name)
                        navController.navigate(Screen.Document.route)
                    }
                )
            }
            composable(Screen.FileBrowser.route) {
                FileBrowserScreen(
                    onFileSelected = { document ->
                        documentViewModel.setDocument(document)
                        navController.navigate(Screen.Document.route)
                    }
                )
            }
            composable(Screen.Document.route) {
                currentDocument?.let { doc ->
                    DocumentScreen(document = doc)
                } ?: run {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}
