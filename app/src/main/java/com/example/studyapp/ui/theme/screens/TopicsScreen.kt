package com.example.studyapp.ui.theme.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studyapp.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.topics_search),
                        modifier = Modifier.padding(start = 16.dp, end = 24.dp)
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.menu),
                        modifier = Modifier.padding(start = 24.dp, end = 16.dp)
                    )
                },
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
            )

        }
    ) { innerPadding ->
    }

}

@Preview
@Composable
private fun SubtopicScreenPreview() {
    TopicsScreen()
}
