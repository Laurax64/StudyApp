package com.example.studyapp.study

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToLog
import com.example.studyapp.data.study.Topic
import com.example.studyapp.data.study.TopicWithProgress
import com.example.studyapp.ui.study.topics.TopicsScreen
import com.example.studyapp.ui.study.topics.TopicsUiState
import com.example.studyapp.utils.DeviceConfigurationOverride
import com.example.studyapp.utils.DeviceSize
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [TopicsScreen].
 *
 * It contains methods for each relevant screen height and width for the different uiStates.
 * The assertions in each method are grouped by the different views defined in the
 * [TopicsScreen Figma design file](https://www.figma.com/design/PFv6qgJRGjVoNkekrOewZM/StudyApp?node-id=486-112211&t=JPnkfQGNJnCnpTBx-0).
 *
 */
class TopicsScreenTest {
    private val successUiState = TopicsUiState.Success(
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
            )
        )
    )

    private val loadingUiState = TopicsUiState.Loading


    @get:Rule
    val composeTestRule = createComposeRule()

    /**
     * Tests the topics screen for compact width and expanded height when
     * the uiState is [TopicsUiState.Success].
     */
    @Test
    fun testTopicsScreen_CompactWidthExpandedHeight_Success() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.COMPACT_WIDTH_EXPANDED_HEIGHT,
            ) {
                TopicsScreen(
                    uiState = successUiState,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        composeTestRule.onRoot().printToLog(
            "TopicsScreenTest"
        )
        // Base
        composeTestRule.onNodeWithText("Dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cats").assertIsDisplayed()
        composeTestRule.onNodeWithText("Horses").assertIsDisplayed()
        composeTestRule.onNodeWithTag("FloatingActionButton").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search in topics").assertIsDisplayed()
        // Search
        composeTestRule.onNodeWithText("Search in topics").performClick()
        composeTestRule.onNodeWithContentDescription("Close search").performClick()
        // Create
        composeTestRule.onNodeWithTag("FloatingActionButton").performClick()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    /**
     * Tests the topics screen for compact width and expanded height when
     * the uiState is [TopicsUiState.Loading].
     */
    @Test
    fun testTopicsScreen_CompactWidthExpandedHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.COMPACT_WIDTH_EXPANDED_HEIGHT,
            ) {
                TopicsScreen(
                    uiState = loadingUiState,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        // Loading
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        composeTestRule.onNodeWithTag("FloatingActionButton").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Search in topics").assertIsNotDisplayed()
    }

    /**
     * Tests the topics screen for medium width and expanded height when
     * the uiState is [TopicsUiState.Success].
     */
    @Test
    fun testTopicsScreen_MediumWidthExpandedHeight_Success() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.MEDIUM_WIDTH_EXPANDED_HEIGHT,
            ) {
                TopicsScreen(
                    uiState = successUiState,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        // Base
        composeTestRule.onNodeWithText("Dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cats").assertIsDisplayed()
        composeTestRule.onNodeWithText("Horses").assertIsDisplayed()
        composeTestRule.onNodeWithTag("MediumFloatingActionButton").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search in topics")
        // Search
        composeTestRule.onNodeWithText("Search in topics").performClick()
        composeTestRule.onNodeWithContentDescription("Close search").performClick()
        // Create
        composeTestRule.onNodeWithTag("MediumFloatingActionButton").performClick()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()

    }

    @Test
    fun testTopicsScreen_MediumWidthExpandedHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.MEDIUM_WIDTH_EXPANDED_HEIGHT
            ) {
                TopicsScreen(
                    uiState = TopicsUiState.Loading,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        // Loading
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        composeTestRule.onNodeWithTag("MediumFloatingActionButton").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Search in topics").assertIsNotDisplayed()
    }

    @Test
    fun testTopicsScreen_ExpandedWidthCompactHeight_Success() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.EXPANDED_WIDTH_COMPACT_HEIGHT
            ) {
                TopicsScreen(
                    uiState = successUiState,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        // Base
        composeTestRule.onNodeWithText("Dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cats").assertIsDisplayed()
        composeTestRule.onNodeWithText("Horses").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LargeFloatingActionButton").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search in topics")
        composeTestRule.onNodeWithText("Select a topic")
        // Search
        composeTestRule.onNodeWithText("Search in topics").performClick()
        composeTestRule.onNodeWithContentDescription("Close search").performClick()
        // Create
        composeTestRule.onNodeWithTag("LargeFloatingActionButton").performClick()
        composeTestRule.onNodeWithText("Save").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cancel").assertIsDisplayed()
    }

    @Test
    fun testTopicsScreen_ExpandedWidthCompactHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.MEDIUM_WIDTH_EXPANDED_HEIGHT
            ) {
                TopicsScreen(
                    uiState = TopicsUiState.Loading,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        // Loading
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        composeTestRule.onNodeWithTag("LargeFloatingActionButton").assertIsNotDisplayed()
        composeTestRule.onNodeWithText("Search in topics").assertIsNotDisplayed()
    }
}