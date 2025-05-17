package com.example.studyapp.ui.screens.subtopic

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.studyapp.R
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.components.study.SaveSubtopicDialog


private enum class DialogType {
    EDIT_SUBTOPIC,
    DELETE_SUBTOPIC
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SubtopicScaffold(
    subtopic: Subtopic,
    updateSubtopic: (Subtopic) -> Unit,
    deleteSubtopic: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator()
    val paneAdaptedValue = scaffoldNavigator.scaffoldState.currentState.primary
    var showSearchBar by rememberSaveable { mutableStateOf(false) }
    var dialogType by rememberSaveable {
        mutableStateOf<DialogType?>(
            null
        )
    }
    if (dialogType == DialogType.EDIT_SUBTOPIC && paneAdaptedValue == PaneAdaptedValue.Hidden) {
        SaveSubtopicDialog(
            titleId = R.string.create_subtopic,
            onDismiss = { dialogType = null },
            isFullScreenDialog = false,
            modifier = modifier,
            saveSubtopic = { title, description, imageUri ->
                updateSubtopic(
                    subtopic.copy(
                        title = title,
                        description = description,
                        imageUri = imageUri
                    )
                )
            },
        )
    } else {
        if (dialogType == DialogType.DELETE_SUBTOPIC)
            Dele
    } else {
        Scaffold(
            modifier = modifier,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = subtopic.title,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_24),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.go_back_to_subtopics),
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { navigateBack() })
                    }, actions = {
                        MoreActionsMenu(
                            shareSubtopic = { /* TODO: Implement share functionality */ },
                            editSubtopic = { showDialog = true },
                            deleteSubtopic = {
                                deleteSubtopic()
                                navigateBack()
                            })
                    })
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(paddingValues = innerPadding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AsyncImage(
                    model = subtopic.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = subtopic.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
}