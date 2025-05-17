package com.example.studyapp.ui.screens.subtopics

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.PlaceholderColumn
import com.example.studyapp.ui.components.StudyAppSearchBar

@Composable
fun SubtopicsPaneContent(
    modifier: Modifier = Modifier,
    subtopics: List<Subtopic>?,
    navigateToSubtopic: (Int) -> Unit,
    closeSearchBar: () -> Unit,
    showSearchBar: Boolean,
    topicTitle: String
) {
    if (subtopics == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        val filteredSubtopics by rememberSaveable { mutableStateOf(subtopics) }
        if (showSearchBar) {
            SubtopicsSearchBar(
                modifier = modifier,
                navigateToSubtopic = navigateToSubtopic,
                closeSearchBar = closeSearchBar,
                subtopics = filteredSubtopics,
                topicTitle = topicTitle
            )
        } else {
            FilterableSubtopicsColumn(
                subtopics = filteredSubtopics,
                navigateToSubtopic = navigateToSubtopic,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun FilterableSubtopicsColumn(
    subtopics: List<Subtopic>,
    navigateToSubtopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var showOnlyNotChecked by rememberSaveable { mutableStateOf(false) }
    var showOnlyBookmarked by rememberSaveable { mutableStateOf(false) }
    val filteredSubtopics = subtopics.filter {
        !(it.checked && showOnlyNotChecked) && (it.bookmarked || !showOnlyBookmarked)
    }
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (subtopics.isNotEmpty()) {
            FilteredChipsRow(
                showOnlyNotChecked = showOnlyNotChecked,
                toggleShowOnlyNotChecked = { showOnlyNotChecked = !showOnlyNotChecked },
                showOnlyBookmarked = showOnlyBookmarked,
                toggleShowOnlyBookmarked = { showOnlyBookmarked = !showOnlyBookmarked }
                )
            SubtopicsLazyColumn(
                filteredSubtopics = filteredSubtopics,
                navigateToSubtopic = navigateToSubtopic,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PlaceholderColumn(
                textId = R.string.no_subtopics_exist,
                iconId = R.drawable.outline_subtitles_24,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}


@Composable
private fun SubtopicsSearchBar(
    modifier: Modifier = Modifier,
    navigateToSubtopic: (Int) -> Unit,
    subtopics: List<Subtopic>,
    topicTitle: String,
    closeSearchBar: () -> Unit,
) {
    StudyAppSearchBar(
        modifier = modifier,
        items = subtopics,
        closeSearchBar = closeSearchBar,
        itemLabel = { it.title },
        placeholderText = stringResource(R.string.search_in, topicTitle),
    ) {
        FilterableSubtopicsColumn(
            subtopics = it,
            navigateToSubtopic = navigateToSubtopic,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun FilteredChipsRow(
    showOnlyNotChecked: Boolean,
    toggleShowOnlyNotChecked: () -> Unit,
    showOnlyBookmarked: Boolean,
    toggleShowOnlyBookmarked: () -> Unit
) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            onClick = toggleShowOnlyNotChecked,
            label = { Text(text = stringResource(R.string.unchecked)) },
            selected = showOnlyNotChecked
        )
        FilterChip(
            onClick = toggleShowOnlyBookmarked,
            label = { Text(text = stringResource(R.string.bookmarked)) },
            selected = showOnlyBookmarked
        )
    }
}


@Composable
private fun SubtopicsLazyColumn(
    filteredSubtopics: List<Subtopic>,
    navigateToSubtopic: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(filteredSubtopics) { subtopic ->
            SubtopicListItem(
                subtopic = subtopic,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navigateToSubtopic(subtopic.id) }
            )
        }
    }
}

@Composable
private fun SubtopicListItem(subtopic: Subtopic, modifier: Modifier) {
    ListItem(
        headlineContent = { Text(subtopic.title, overflow = Ellipsis, maxLines = 1) },
        modifier = modifier,
        leadingContent = {
            Icon(
                painter = painterResource(
                    id = if (subtopic.bookmarked) {
                        R.drawable.baseline_bookmark_24
                    } else {
                        R.drawable.baseline_bookmark_border_24
                    }
                ),
                modifier = Modifier,
                contentDescription = stringResource(
                    id = if (subtopic.bookmarked) {
                        R.string.bookmarked
                    } else {
                        R.string.bookmarked
                    }
                )
            )
        },
        trailingContent = {
            Checkbox(
                checked = subtopic.checked,
                enabled = false,
                onCheckedChange = null,
            )
        },
    )
}