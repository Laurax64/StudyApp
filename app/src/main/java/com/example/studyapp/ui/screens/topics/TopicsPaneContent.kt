package com.example.studyapp.ui.screens.topics

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.DockedSearchBar
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.study.TopicsLazyColumn

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopicsPaneContent(
    topics: List<Topic>?,
    modifier: Modifier = Modifier,
    navigateToTopic: (Int) -> Unit,
    closeSearchBar: () -> Unit,
    showSearchBar: Boolean
) {
    if (topics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator()
        }
    } else if (showSearchBar) {
        TopicsSearchBar(
            modifier = modifier.fillMaxWidth(),
            navigateToTopic = navigateToTopic,
            closeSearchBar = closeSearchBar,
            topics = topics
        )
    } else {
        if (topics.isEmpty()) {
            PlaceholderColumn(
                textId = R.string.no_topics_exist,
                iconId = R.drawable.outline_topic_24,
                modifier = modifier.fillMaxSize()
            )
        } else {
            TopicsLazyColumn(
                topics = topics,
                navigateToTopic = navigateToTopic,
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun TopicsSearchBar(
    modifier: Modifier = Modifier,
    navigateToTopic: (Int) -> Unit,
    topics: List<Topic>,
    closeSearchBar: () -> Unit
) {
    DockedSearchBar(
        modifier = modifier,
        items = topics,
        closeSearchBar = closeSearchBar,
        itemLabel = { it.title },
        placeholderText = stringResource(R.string.search_in_topics)
    ) {
        TopicsLazyColumn(
            topics = it,
            navigateToTopic = navigateToTopic,
            modifier = modifier
                .padding(horizontal = 8.dp)
                .fillMaxSize()
        )
    }
}


