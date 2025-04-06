package com.example.studyapp.utils

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.runtime.Composable


@Composable
fun ImagePicker() {

    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->

        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly))

}
