package com.example.studyapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shared.Greeting
import com.example.studyapp.ui.StudyApp
import com.example.studyapp.ui.rememberStudyAppState
import com.example.studyapp.ui.theme.StudyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("MainActivity", "Hello from shared module: " + (Greeting().greet()))
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


