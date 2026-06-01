package com.elephenman.docviewer.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.elephenman.docviewer.ui.DocumentScreen
import com.elephenman.docviewer.ui.DocumentViewModel
import com.elephenman.docviewer.ui.filebrowser.FileBrowserScreen
import androidx.hilt.navigation.compose.hiltViewModel

sealed class Screen(val route: String) {
    data object FileBrowser : Screen("file_browser")
    data object Document : Screen("document")
}

@Composable
fun DocViewerNavHost(
    navController: NavHostController = rememberNavController()
) {
    val documentViewModel: DocumentViewModel = hiltViewModel()
    val currentDocument by documentViewModel.currentDocument.collectAsState()

    NavHost(navController = navController, startDestination = Screen.FileBrowser.route) {
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
                // Navigate back if no document is selected
                LaunchedEffect(Unit) {
                    navController.popBackStack()
                }
            }
        }
    }
}
