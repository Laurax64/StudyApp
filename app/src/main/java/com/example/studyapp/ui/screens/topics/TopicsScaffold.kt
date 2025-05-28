package com.example.studyapp.ui.screens.topics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.Topic
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.study.SaveTopicDialog

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TopicsScaffold(
    topics: List<Topic>,
    saveTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    var showDialog by rememberSaveable { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()
    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    if (showDialog) {
        SaveTopicDialog(onDismiss = { showDialog = false }, topic = null, onSave = saveTopic)
    }
    Scaffold(
        modifier = modifier,
        topBar = {
            TopSearchBar(
                state = searchBarState,
                inputField = TODO(),
                modifier = TODO(),
                shape = TODO(),
                colors = TODO(),
                tonalElevation = TODO(),
                shadowElevation = TODO(),
                windowInsets = TODO(),
                scrollBehavior = TODO()
            )
        },
        floatingActionButton = {
            CreateTopicFAB(saveTopic = { showDialog = true })
        },
    ) { innerPadding ->
        NavigableListDetailPaneScaffold(
            navigator = scaffoldNavigator,
            listPane = {
                AnimatedPane {
                    TopicsPaneContent(
                        navigateToTopic = {
                            // Not scaffoldNavigator.navigateTo because the app needs to
                            // change more than just the detail pane
                            navigateToSubtopics(it)
                        },
                        closeSearchBar = { showSearchBar = false },
                        showSearchBar = showSearchBar,
                        topics = topics,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopicsTopAppBar(
    modifier: Modifier = Modifier,
    onSearch: () -> Unit,
) {


    TopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.baseline_search_24),
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = stringResource(R.string.topics_search),
                modifier = Modifier.clickable { onSearch() })
        }, actions = {
            Icon(
                painter = painterResource(R.drawable.baseline_account_circle_24),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = stringResource(R.string.topics_search)
            )
        }, title = { Text(text = stringResource(R.string.app_name)) }, modifier = modifier
    )
}

@Composable
private fun CreateTopicFAB(saveTopic: () -> Unit, modifier: Modifier = Modifier) {
    ExtendedFloatingActionButton(
        onClick = saveTopic,
        modifier = modifier,
        icon = {
            Icon(
                painter = painterResource(R.drawable.baseline_add_24),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = stringResource(R.string.create_topic),
                modifier = Modifier.size(24.dp)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.create_topic),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
    )
}
