package com.voetzsch.studyapp.ui.aiAssistant

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun AIAssistantScreen(
    modifier: Modifier = Modifier
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                loadUrl("https://gemini.google.com")
            }
        }
    )
}
