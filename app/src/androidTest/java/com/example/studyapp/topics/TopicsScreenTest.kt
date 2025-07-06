package com.example.studyapp.topics

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.studyapp.data.Topic
import com.example.studyapp.data.TopicWithProgress
import com.example.studyapp.ui.topics.TopicsScreen
import com.example.studyapp.ui.topics.TopicsUiState
import com.example.studyapp.utils.DeviceConfigurationOverride
import com.example.studyapp.utils.DeviceSize
import org.junit.Rule
import org.junit.Test

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
        // Assert that each subtopic is displayed.
        composeTestRule.onNodeWithText("Dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cats").assertIsDisplayed()
        composeTestRule.onNodeWithText("Horses").assertIsDisplayed()
        // Assert that the FAB is displayed.
        composeTestRule.onNodeWithTag("FloatingActionButton").assertIsDisplayed()
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
                    uiState = loadingUiState,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        // Assert that the FAB is not displayed.
        composeTestRule.onNodeWithTag("FloatingActionButton").assertIsNotDisplayed()
        // Assert that the search bar is not displayed.
        composeTestRule.onNodeWithText("Search in topics").assertIsNotDisplayed()
    }


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

        // Assert that each subtopic is displayed.
        composeTestRule.onNodeWithText("Dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cats").assertIsDisplayed()
        composeTestRule.onNodeWithText("Horses").assertIsDisplayed()
        // Assert that the medium FAB is displayed.
        composeTestRule.onNodeWithTag("MediumFloatingActionButton").assertIsDisplayed()
        // Assert that the search bar is displayed.
        composeTestRule.onNodeWithText("Search in topics")
    }

    @Test
    fun testTopicsScreen_MediumWidthExpandedHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.MEDIUM_WIDTH_EXPANDED_HEIGHT
            ) {
                TopicsScreen(
                    uiState = loadingUiState,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        // Assert that the medium FAB is not displayed.
        composeTestRule.onNodeWithTag("MediumFloatingActionButton").assertIsNotDisplayed()
        // Assert that the search bar is not displayed.
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

        // Assert that each subtopic is displayed.
        composeTestRule.onNodeWithText("Dogs").assertIsDisplayed()
        composeTestRule.onNodeWithText("Cats").assertIsDisplayed()
        composeTestRule.onNodeWithText("Horses").assertIsDisplayed()
        // Assert that the large FAB is displayed.
        composeTestRule.onNodeWithTag("LargeFloatingActionButton").assertIsDisplayed()
        // Assert that the search bar is displayed.
        composeTestRule.onNodeWithText("Search in topics")

    }

    @Test
    fun testTopicsScreen_ExpandedWidthCompactHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.MEDIUM_WIDTH_EXPANDED_HEIGHT
            ) {
                TopicsScreen(
                    uiState = loadingUiState,
                    addTopic = {},
                    navigateToSubtopics = {},
                )
            }
        }
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertIsDisplayed()
        // Assert that the large FAB is not displayed.
        composeTestRule.onNodeWithTag("LargeFloatingActionButton").assertIsNotDisplayed()
        // Assert that the search bar is not displayed.
        composeTestRule.onNodeWithText("Search in topics").assertIsNotDisplayed()
    }


}