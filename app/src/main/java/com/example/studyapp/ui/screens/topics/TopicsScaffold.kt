package com.example.studyapp.ui.screens.topics

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.SearchAppBar
import com.example.studyapp.ui.components.study.AdaptiveFAB
import com.example.studyapp.ui.components.study.SaveTopicDialog

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun TopicsScaffold(
    topics: List<Topic>,
    saveTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    var showSearchView by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val filteredTopics by rememberSaveable { mutableStateOf(topics) }
    calculateFromAdaptiveInfo(adaptiveInfo = currentWindowAdaptiveInfo())
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
                modifier = Modifier.padding(bottom = 16.dp),
                iconId = R.drawable.baseline_add_24,
                contentDescriptionId = R.string.create_topic
            )
        },
        floatingActionButtonPosition = FabPosition.Start
    ) { innerPadding ->
        NavigableListDetailPaneScaffold(
            navigator = scaffoldNavigator,
            listPane = {
                AnimatedPane {
                    TopicsPaneContent(
                        topics = filteredTopics,
                        navigateToTopic = {
                            // Not scaffoldNavigator.navigateTo because the app needs to
                            // change more than just the detail pane
                            navigateToSubtopics(it)
                        },
                        closeSearchBar = { showSearchView = false },
                        showSearchBar = showSearchView,
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    PlaceholderColumn(
                        textId = if (topics.isEmpty()) {
                            R.string.no_subtopics_exist
                        } else {
                            R.string.select_a_topic
                        },
                        iconId = R.drawable.outline_subtitles_24,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

