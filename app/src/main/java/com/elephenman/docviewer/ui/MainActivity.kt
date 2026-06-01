package com.elephenman.docviewer.ui

import android.content.Intent
import android.net.Uri
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

        val externalUri = extractExternalUri(intent)

        setContent {
            DocViewerTheme {
                DocViewerNavHost(externalUri = externalUri)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val externalUri = extractExternalUri(intent)
        externalUri?.let {
            setContent {
                DocViewerTheme {
                    DocViewerNavHost(externalUri = it)
                }
            }
        }
    }

    private fun extractExternalUri(intent: Intent): Uri? {
        return when (intent.action) {
            Intent.ACTION_VIEW -> intent.data
            Intent.ACTION_SEND -> intent.getParcelableExtra(Intent.EXTRA_STREAM)
            else -> null
        }
    }
}
