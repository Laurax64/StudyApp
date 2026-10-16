package com.voetzsch.studyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.voetzsch.studyapp.ui.StudyApp
import com.voetzsch.studyapp.ui.rememberStudyAppState
import com.voetzsch.studyapp.ui.theme.StudyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appState = rememberStudyAppState()
            StudyAppTheme {
                StudyApp(appState = appState)
            }
        }
    }
}


