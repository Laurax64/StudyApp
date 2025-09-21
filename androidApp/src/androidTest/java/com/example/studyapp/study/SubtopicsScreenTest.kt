package com.example.studyapp.study

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.studyapp.data.study.Subtopic
import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicWithProgress
import com.example.studyapp.ui.study.subtopics.SubtopicsScreen
import com.example.studyapp.ui.study.subtopics.SubtopicsUiState
import com.example.studyapp.utils.DeviceConfigurationOverride
import com.example.studyapp.utils.DeviceSize
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [SubtopicsScreen].
 *
 * It contains methods for each relevant screen height and width for the different uiStates.
 * The assertions in each method are grouped by the different views defined in the
 * [TopicsScreen Figma design file](https://www.figma.com/design/PFv6qgJRGjVoNkekrOewZM/StudyApp?node-id=1-2&p=f&t=JPnkfQGNJnCnpTBx-0).
 *
 */
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
        selectedTopic = Topic(id = 0, title = "Dogs")
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
        // Base
        composeTestRule.onNodeWithTag("SubtopicsToolbar").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Golden Retriever").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Labrador Retriever").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "German Shepherd").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Unchecked").assertIsNotSelected()
        composeTestRule.onNodeWithText("Bookmarked").assertIsNotSelected()
        // Edit
        composeTestRule.onNodeWithContentDescription("Open edit topic dialog").performClick()
        composeTestRule.onNodeWithText("Edit topic").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithText("Open edit topic dialog").assertIsNotDisplayed()
        // Delete
        composeTestRule.onNodeWithContentDescription("Open delete topic dialog").performClick()
        composeTestRule.onNodeWithText("Delete topic?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").performClick()
        composeTestRule.onNodeWithText("Delete topic?").assertIsNotDisplayed()
        // Create
        composeTestRule.onNodeWithContentDescription("Create subtopic").performClick()
        composeTestRule.onNodeWithText("Create subtopic").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Cancel").performClick()
        composeTestRule.onNodeWithText("Create subtopic").assertIsNotDisplayed()
        // Search
        composeTestRule.onNodeWithContentDescription("Subtopics search").performClick()
        composeTestRule.onNodeWithText("Search in Dogs").assertIsDisplayed()
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
        // Loading
        composeTestRule.onNodeWithTag("SubtopicsToolbar").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        composeTestRule.onNodeWithText(text = "Dogs").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("Unchecked").assertIsNotDisplayed()
        composeTestRule.onNodeWithTag("Bookmarked").assertIsNotDisplayed()
    }
}