package com.example.studyapp.ui.study.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.ui.theme.StudyAppTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun FabWithConnectedButtons() {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.BottomEnd
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedVisibility(visible = expanded) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(onClick = { /* Action 1 */ }, shape = MaterialTheme.shapes.large) {
                        Text("I knew this")
                    }
                    Button(onClick = { /* Action 2 */ }, shape = MaterialTheme.shapes.large) {
                        Text("Action 2")
                    }
                    Button(onClick = { /* Action 3 */ }, shape = MaterialTheme.shapes.large) {
                        Text("Action 3")
                    }
                }
            }

            FloatingActionButton(onClick = { expanded = !expanded }) {
                Icon(
                    painter = if (expanded) painterResource(R.drawable.baseline_close_24) else painterResource(
                        R.drawable.baseline_check_24
                    ),
                    contentDescription = if (expanded) "Close actions" else "Open actions"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FabWithConnectedButtonsPreview() {
    StudyAppTheme {
        FabWithConnectedButtons()
    }
}
