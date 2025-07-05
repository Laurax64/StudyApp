package com.example.studyapp.ui.topics

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.ui.components.DockedSearchBar
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.SearchAppBar
import com.example.studyapp.ui.components.study.AdaptiveFAB
import com.example.studyapp.ui.components.study.LoadingIndicatorBox
import com.example.studyapp.ui.components.study.SaveTopicDialog
import com.example.studyapp.ui.components.study.TopicsLazyColumn
import com.example.studyapp.ui.theme.StudyAppTheme

@Composable
internal fun TopicsScreen(
    topicsViewModel: TopicsViewModel,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by topicsViewModel.uiState.collectAsStateWithLifecycle()
    TopicsScreen(
        uiState = uiState,
        addTopic = topicsViewModel::addTopic,
        navigateToSubtopics = navigateToTopic,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopicsScreen(
    uiState: TopicsUiState,
    addTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        TopicsUiState.Loading ->
            LoadingIndicatorBox()

        is TopicsUiState.Success ->
            TopicsScaffold(
                topicsWithProgress = uiState.topicsWithProgress,
                saveTopic = addTopic,
                navigateToSubtopics = navigateToSubtopics,
                modifier = modifier
            )
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun TopicsScaffold(
    topicsWithProgress: List<TopicWithProgress>,
    saveTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    var showSearchView by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }

    if (showDialog) {
        SaveTopicDialog(
            onDismiss = { showDialog = false },
            topic = null,
            onSave = {
                saveTopic(it)
                showDialog = false
            }
        )
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            if (!showSearchView) {
                SearchAppBar(
                    placeholderText = stringResource(R.string.search_in_topics),
                    openSearchView = { showSearchView = true },
                    modifier = Modifier.fillMaxWidth()
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
                    AnimatedContent(targetState = topicsWithProgress) {
                        TopicsPaneContent(
                            topicsWithProgress = it,
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
                    AnimatedContent(targetState = topicsWithProgress) {
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
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        )
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun TopicsPaneContent(
    topicsWithProgress: List<TopicWithProgress>,
    modifier: Modifier = Modifier,
    navigateToTopic: (Int) -> Unit,
    closeSearchBar: () -> Unit,
    showSearchBar: Boolean
) {
    if (showSearchBar) {
        TopicsSearchBar(
            modifier = modifier.fillMaxWidth(),
            navigateToTopic = navigateToTopic,
            closeSearchBar = closeSearchBar,
            topicsWithProgress = topicsWithProgress
        )
    } else {
        if (topicsWithProgress.isEmpty()) {
            PlaceholderColumn(
                textId = R.string.no_topics_exist,
                iconId = R.drawable.outline_topic_24,
                modifier = modifier.fillMaxSize()
            )
        } else {
            TopicsLazyColumn(
                topicsWithProgress = topicsWithProgress,
                navigateToTopic = navigateToTopic,
                modifier = modifier
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun TopicsSearchBar(
    modifier: Modifier = Modifier,
    navigateToTopic: (Int) -> Unit,
    topicsWithProgress: List<TopicWithProgress>,
    closeSearchBar: () -> Unit
) {
    DockedSearchBar(
        modifier = modifier,
        items = topicsWithProgress,
        closeSearchBar = closeSearchBar,
        itemLabel = { it.topic.title },
        placeholderText = stringResource(R.string.search_in_topics)
    ) {
        TopicsLazyColumn(
            topicsWithProgress = it,
            navigateToTopic = navigateToTopic,
            modifier = modifier
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
            uiState = TopicsUiState.Success(
                topicsWithProgress = listOf(
                    TopicWithProgress(
                        topic = Topic(1, "Dogs"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(2, "Cats"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(3, "Horses"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(4, "Rabbits"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(5, "Fish"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(6, "Birds"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(7, "Hamsters"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(8, "Guinea pigs"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(9, "Turtles"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(10, "Elephants"),
                        checked = false
                    )
                )
            ),
            addTopic = {},
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
            uiState = TopicsUiState.Loading,
            navigateToSubtopics = {},
            addTopic = {},
        )
    }
}