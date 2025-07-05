package com.example.studyapp.topics

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.example.studyapp.DeviceConfigurationOverride
import com.example.studyapp.DeviceSize
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.ui.topics.TopicsScreen
import com.example.studyapp.ui.topics.TopicsUiState
import org.junit.Rule
import org.junit.Test

class TopicsScreenTest {
    private val topics = listOf(
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
        )
    )

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testTopicsScreen_CompactWidthExpandedHeight_Success() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.COMPACT_WIDTH_EXPANDED_HEIGHT,
            ) {
                TopicsScreen(
                    uiState = TopicsUiState.Success(topicsWithProgress = topics),
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        composeTestRule.onRoot(
            useUnmergedTree = true
        ).printToLog("currentLabelExists")

        // Assert that each subtopic is displayed.
        composeTestRule.onNodeWithText("Dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cats").assertIsDisplayed()
        composeTestRule.onNodeWithText("Horses").assertIsDisplayed()
        // Assert that the FAB is displayed.
        composeTestRule.onNodeWithContentDescription("Create topic").assertIsDisplayed()
        // Assert that the search bar is displayed.
        composeTestRule.onNodeWithText("Search in topics").assertIsDisplayed()
    }

    @Test
    fun testTopicsScreen_CompactWidthExpandedHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.COMPACT_WIDTH_EXPANDED_HEIGHT,
            ) {
                TopicsScreen(
                    uiState = TopicsUiState.Loading,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        // Assert that the FAB is not displayed.
        composeTestRule.onNodeWithContentDescription("Create topic").assertIsNotDisplayed()
    }
}