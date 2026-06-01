package com.elephenman.docviewer.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.elephenman.docviewer.ui.navigation.DocViewerNavHost
import com.elephenman.docviewer.ui.theme.DocViewerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DocViewerTheme {
                DocViewerNavHost()
            }
        }
    }
}
