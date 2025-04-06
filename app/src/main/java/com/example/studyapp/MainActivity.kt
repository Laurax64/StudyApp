package com.example.studyapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.studyapp.ui.screens.TopicsScreen
import com.example.studyapp.ui.theme.StudyAppTheme
import dagger.hilt.android.AndroidEntryPoint

// TODO : Add Hilt dependencies to the project
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyAppTheme {
                TopicsScreen()
            }
        }
    }
}


