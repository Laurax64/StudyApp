package com.example.studyapp.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.DockedSearchBar
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.SearchAppBar
import com.example.studyapp.ui.components.study.AdaptiveFAB
import com.example.studyapp.ui.components.study.SaveTopicDialog
import com.example.studyapp.ui.components.study.TopicsLazyColumn
import com.example.studyapp.ui.theme.StudyAppTheme
import com.example.studyapp.ui.viewmodels.TopicsViewModel

@Composable
fun TopicsScreen(
    topicsViewModel: TopicsViewModel,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val topics by topicsViewModel.topics.collectAsStateWithLifecycle()
    Log.d("TopicsScreen", "Collected topics: $topics")
    TopicsScreen(
        topics = topics,
        saveTopic = topicsViewModel::saveTopic,
        navigateToSubtopics = navigateToTopic,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopicsScreen(
    topics: List<Topic>?,
    saveTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (topics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LoadingIndicator()
        }
    } else {
        TopicsScaffold(
            topics = topics,
            saveTopic = saveTopic,
            navigateToSubtopics = navigateToSubtopics,
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun TopicsScaffold(
    topics: List<Topic>,
    saveTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    var showSearchView by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        SaveTopicDialog(onDismiss = { showDialog = false }, topic = null, onSave = saveTopic)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            if (!showSearchView) {
                SearchAppBar(
                    placeholderText = stringResource(R.string.search_in_topics),
                    openSearchView = { showSearchView = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
        },
        floatingActionButton = {
            AdaptiveFAB(
                onClick = { showDialog = true },
                iconId = R.drawable.baseline_add_24,
                contentDescriptionId = R.string.create_topic
            )
        },
    ) { innerPadding ->
        NavigableListDetailPaneScaffold(
            navigator = scaffoldNavigator,
            listPane = {
                AnimatedPane {
                    AnimatedContent(targetState = topics) {
                        TopicsPaneContent(
                            topics = it,
                            navigateToTopic = {
                                // Not scaffoldNavigator.navigateTo because the app needs to
                                // change more than just the detail pane
                                navigateToSubtopics(it)
                            },
                            closeSearchBar = { showSearchView = false },
                            showSearchBar = showSearchView,
                        )
                    }
                }
            },
            detailPane = {
                AnimatedPane {
                    AnimatedContent(targetState = topics) {
                        PlaceholderColumn(
                            textId = if (it.isEmpty()) {
                                R.string.no_subtopics_exist
                            } else {
                                R.string.select_a_topic
                            },
                            iconId = R.drawable.outline_subtitles_24,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopicsPaneContent(
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


@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun TopicsScreenPreview() {
    StudyAppTheme {
        TopicsScreen(
            topics = listOf(
                Topic(1, "Dogs", false),
                Topic(2, "Cats", false),
                Topic(3, "Horses", false),
                Topic(4, "Rabbits", false),
                Topic(5, "Fish", false),
                Topic(6, "Birds", false),
                Topic(7, "Hamsters", false),
                Topic(8, "Guinea pigs", false),
                Topic(9, "Turtles", false),
                Topic(10, "Elephants", false)
            ),
            saveTopic = {},
            navigateToSubtopics = {},
        )
    }
}

@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun TopicsScreenLoadingPreview() {
    StudyAppTheme {
        TopicsScreen(
            topics = null,
            navigateToSubtopics = {},
            saveTopic = {},
        )
    }
}