package com.example.studyapp.topics

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.studyapp.data.Subtopic
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.ui.subtopics.SubtopicsScreen
import com.example.studyapp.ui.subtopics.SubtopicsUiState
import com.example.studyapp.utils.DeviceConfigurationOverride
import com.example.studyapp.utils.DeviceSize
import org.junit.Rule
import org.junit.Test

class SubtopicsScreenTest {
    private val subtopics = listOf(
        Subtopic(
            id = 1,
            topicId = 0,
            title = "Golden Retriever",
            description = "Friendly, intelligent, and great with families.",
            checked = false,
            bookmarked = false,
            imageUri = null
        ),
        Subtopic(
            id = 2,
            topicId = 0,
            title = "Labrador Retriever",
            description = "Outgoing, loyal, and super trainable.",
            checked = false,
            bookmarked = false,
            imageUri = null
        ),
        Subtopic(
            id = 3,
            topicId = 0,
            title = "German Shepherd",
            description = "Brave, confident, and excellent working dogs.",
            checked = false,
            bookmarked = false,
            imageUri = null
        )
    )

    private val successUiState = SubtopicsUiState.Success(
        subtopics = subtopics,
        topicsWithProgress = listOf(
            TopicWithProgress(
                topic = Topic(id = 0, title = "Topic 0"),
                checked = true
            )
        ),
        selectedTopic = Topic(id = 0, title = "Android Taint Analysis")
    )

    private val loadingUiState = SubtopicsUiState.Loading

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSubtopicsScreen_CompactWidthExpandedHeight_Success() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.COMPACT_WIDTH_EXPANDED_HEIGHT,
            ) {
                SubtopicsScreen(
                    uiState = successUiState,
                    navigateToSubtopic = {},
                    navigateToTopic = {},
                    navigateBack = {},
                    saveSubtopic = {},
                    deleteTopic = {},
                    updateTopic = {},
                )
            }
        }
        composeTestRule.onNodeWithTag("SubtopicsToolbar").assertIsDisplayed()
        // Assert that each subtopic is displayed.
        composeTestRule.onNodeWithText(text = "Golden Retriever").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Labrador Retriever").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "German Shepherd").assertIsDisplayed()
        // Assert that the app bar title is displayed.
        composeTestRule.onNodeWithText(text = "Android Taint Analysis").assertIsDisplayed()
        // Assert that the filter chips are displayed.
        composeTestRule.onNodeWithText("Unchecked").assertIsNotSelected()
        composeTestRule.onNodeWithText("Bookmarked").assertIsNotSelected()
    }

    @Test
    fun testSubtopicsScreen_CompactWidthExpandedHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.COMPACT_WIDTH_EXPANDED_HEIGHT,
            ) {
                SubtopicsScreen(
                    uiState = loadingUiState,
                    navigateToSubtopic = {},
                    navigateToTopic = {},
                    navigateBack = {},
                    saveSubtopic = {},
                    deleteTopic = {},
                    updateTopic = {},
                )
            }
        }
        composeTestRule.onNodeWithTag("SubtopicsToolbar").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        // Assert that the app bar title is not displayed.
        composeTestRule.onNodeWithText(text = "Android Taint Analysis").assertIsNotDisplayed()
        // Assert that the filter chips are not displayed.
        composeTestRule.onNodeWithTag("Unchecked").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("Bookmarked").assertIsNotDisplayed()
    }
}