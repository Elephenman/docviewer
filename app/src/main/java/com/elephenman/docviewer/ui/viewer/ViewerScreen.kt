package com.elephenman.docviewer.ui.viewer

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.elephenman.docviewer.data.model.Document

@Composable
fun ViewerScreen(
    document: Document,
    viewModel: ViewerViewModel = hiltViewModel()
) {
    LaunchedEffect(document) {
        viewModel.loadDocument(document)
    }

    val htmlContent by viewModel.htmlContent.collectAsState()

    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = false
                webViewClient = WebViewClient()
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)
        },
        modifier = Modifier.fillMaxSize()
    )
}
