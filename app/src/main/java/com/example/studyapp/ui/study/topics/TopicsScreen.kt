package com.example.studyapp.ui.study.topics

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.VisibleForTesting
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicWithProgress
import com.example.studyapp.ui.authentication.AuthenticationAlternative
import com.example.studyapp.ui.authentication.AuthenticationDialog
import com.example.studyapp.ui.authentication.AuthenticationUiState
import com.example.studyapp.ui.authentication.AuthenticationViewModel
import com.example.studyapp.ui.study.components.AdaptiveFAB
import com.example.studyapp.ui.study.components.DockedSearchBar
import com.example.studyapp.ui.study.components.LoadingIndicatorBox
import com.example.studyapp.ui.study.components.PlaceholderColumn
import com.example.studyapp.ui.study.components.SaveTopicDialog
import com.example.studyapp.ui.study.components.SearchAppBar
import com.example.studyapp.ui.study.components.TopicsLazyColumn
import com.example.studyapp.ui.theme.StudyAppTheme

private enum class TopicDialogType {
    CREATE_TOPIC,
    AUTHENTICATION
}


@Composable
internal fun TopicsScreen(
    topicsViewModel: TopicsViewModel,
    authenticationViewModel: AuthenticationViewModel,
    navigateToTopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val topicsUiState by topicsViewModel.uiState.collectAsStateWithLifecycle()
    val authenticationUiState by authenticationViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    TopicsScreen(
        topicsUiState = topicsUiState,
        addTopic = topicsViewModel::addTopic,
        navigateToSubtopics = navigateToTopic,
        authenticationUiState = authenticationUiState,
        initiateAuthentication = {
            authenticationViewModel.initiateAuthentication(
                context = context,
                authenticationAlternative = it
            )
        },
        modifier = modifier
    )
}

@VisibleForTesting
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TopicsScreen(
    topicsUiState: TopicsUiState,
    authenticationUiState: AuthenticationUiState,
    addTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (topicsUiState) {
        TopicsUiState.Loading ->
            LoadingIndicatorBox()

        is TopicsUiState.Success ->
            if (authenticationUiState is AuthenticationUiState.Success) {
                TopicsScaffold(
                    topicsWithProgress = topicsUiState.topicsWithProgress,
                    saveTopic = addTopic,
                    navigateToSubtopics = navigateToSubtopics,
                    modifier = modifier,
                    authenticationUiState = authenticationUiState,
                    initiateAuthentication = initiateAuthentication,
                )
            }
    }
}


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
private fun TopicsScaffold(
    topicsWithProgress: List<TopicWithProgress>,
    saveTopic: (Topic) -> Unit,
    navigateToSubtopics: (Int) -> Unit,
    authenticationUiState: AuthenticationUiState.Success,
    initiateAuthentication: (AuthenticationAlternative) -> Unit,
    modifier: Modifier = Modifier,
) {
    var dialogType by rememberSaveable { mutableStateOf<TopicDialogType?>(null) }
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    var showSearchView by rememberSaveable { mutableStateOf(false) }
    val isScreenWidthCompact = !currentWindowAdaptiveInfo().windowSizeClass
        .isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)
    when (dialogType) {
        TopicDialogType.CREATE_TOPIC -> SaveTopicDialog(
            topic = null,
            onDismiss = { dialogType = null },
            onSave = {
                saveTopic(it)
                dialogType = null
            }
        )

        TopicDialogType.AUTHENTICATION -> AuthenticationDialog(
            closeDialog = { dialogType = null },
            authenticationUiState = authenticationUiState,
            initiateAuthentication = initiateAuthentication
        )

        null -> {}
    }


    if (!(dialogType == TopicDialogType.AUTHENTICATION && isScreenWidthCompact)) {
        Scaffold(
            modifier = modifier,
            topBar = {
                if (!showSearchView) {
                    SearchAppBar(
                        placeholderText = stringResource(R.string.search_in_topics),
                        openSearchView = { showSearchView = true },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            UserAvatarIcon(
                                uiState = authenticationUiState,
                                modifier = it
                            )
                        },
                        showAuthenticationDialog = {
                            dialogType = TopicDialogType.AUTHENTICATION
                        }
                    )
                }
            },
            floatingActionButton = {
                AdaptiveFAB(
                    onClick = { dialogType = TopicDialogType.CREATE_TOPIC },
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
                                navigateToTopic = { topicId ->
                                    // Not scaffoldNavigator.navigateTo because the app needs to
                                    // change more than just the detail pane
                                    navigateToSubtopics(topicId)
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
}

@Composable
private fun UserAvatarIcon(uiState: AuthenticationUiState, modifier: Modifier = Modifier) {
    if (uiState is AuthenticationUiState.Success && uiState.userIsSignedIn) {
        val userId = uiState.email ?: uiState.phoneNumber
        if (uiState.profilePictureUri != null) {
            AsyncImage(
                model = uiState.profilePictureUri,
                contentDescription =
                    userId?.let {
                        stringResource(R.string.signed_in_as, userId)
                    },
                modifier = modifier
            )
        } else if (uiState.email != null) {
            Text(
                text = uiState.email.first().toString(),
                style = MaterialTheme.typography.displayMedium,
                modifier = modifier
            )
        } else {
            Text("?") // TODO: Fix this
        }

    } else {
        Icon(
            painter = painterResource(R.drawable.baseline_account_circle_24),
            contentDescription = stringResource(R.string.open_login),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
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


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun TopicsScreenPreview() {
    StudyAppTheme {
        TopicsScreen(
            authenticationUiState = AuthenticationUiState.Success(
                userIsSignedIn = false,
                profilePictureUri = null,
                email = "ExampleEmail@example.com",
                phoneNumber = null,
            ),
            topicsUiState = TopicsUiState.Success(
                topicsWithProgress = listOf(
                    TopicWithProgress(
                        topic = Topic(id = 1, title = "Dogs"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(2, title = "Cats"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(3, title = "Horses"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(4, title = "Rabbits"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(5, title = "Fish"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(6, title = "Birds"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(7, title = "Hamsters"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(8, title = "Guinea pigs"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(9, title = "Turtles"),
                        checked = false
                    ),
                    TopicWithProgress(
                        topic = Topic(10, title = "Elephants"),
                        checked = false
                    )
                )
            ),
            addTopic = {},
            navigateToSubtopics = {},
            initiateAuthentication = { }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Preview(showSystemUi = true)
@PreviewLightDark
@PreviewScreenSizes
@PreviewDynamicColors
@PreviewFontScale
@Composable
private fun TopicsScreenLoadingPreview() {
    StudyAppTheme {
        TopicsScreen(
            authenticationUiState = AuthenticationUiState.Loading,
            topicsUiState = TopicsUiState.Loading,
            initiateAuthentication = {},
            navigateToSubtopics = {},
            addTopic = {},
        )
    }
}