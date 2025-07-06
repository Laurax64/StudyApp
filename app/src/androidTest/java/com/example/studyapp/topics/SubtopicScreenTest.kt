package com.example.studyapp.topics

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.subtopic.SubtopicScreen
import com.example.studyapp.ui.subtopic.SubtopicUiState
import com.example.studyapp.utils.DeviceConfigurationOverride
import com.example.studyapp.utils.DeviceSize
import org.junit.Rule
import org.junit.Test

class SubtopicScreenTest {
    private val successUiState = SubtopicUiState.Success(
        subtopic = Subtopic(
            id = 1,
            title = "Subtopic Title",
            description = "Subtopic Description",
            checked = false,
            bookmarked = false,
            topicId = 1,
            imageUri = null,
        ),
        previousSubtopicId = 0,
        nextSubtopicId = 2,
    )

    private val loadingUiState = SubtopicUiState.Loading

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testSubtopicScreen_CompactWidthExpandedHeight_Success() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.COMPACT_WIDTH_EXPANDED_HEIGHT,
            ) {
                SubtopicScreen(
                    uiState = successUiState,
                    updateSubtopic = {},
                    deleteSubtopic = {},
                    navigateBack = {},
                    navigateToSubtopic = {},
                    navigateBackToSubtopics = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Subtopic Title").assertExists()
        composeTestRule.onNodeWithText("Subtopic Description").assertExists()
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbar")
    }

    @Test
    fun testSubtopicScreen_CompactWidthExpandedHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.COMPACT_WIDTH_EXPANDED_HEIGHT,
            ) {
                SubtopicScreen(
                    uiState = loadingUiState,
                    updateSubtopic = {},
                    deleteSubtopic = {},
                    navigateBack = {},
                    navigateToSubtopic = {},
                    navigateBackToSubtopics = {}
                )
            }
        }
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbar").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertExists()
    }

    @Test
    fun testSubtopicScreen_MediumWidthExpandedHeight_Success() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.MEDIUM_WIDTH_EXPANDED_HEIGHT
            ) {
                SubtopicScreen(
                    uiState = successUiState,
                    updateSubtopic = {},
                    deleteSubtopic = {},
                    navigateBack = {},
                    navigateToSubtopic = {},
                    navigateBackToSubtopics = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Subtopic Title").assertExists()
        composeTestRule.onNodeWithText("Subtopic Description").assertExists()
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbarRow")
    }

    @Test
    fun testSubtopicScreen_MediumWidthExpandedHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.MEDIUM_WIDTH_EXPANDED_HEIGHT
            ) {
                SubtopicScreen(
                    uiState = loadingUiState,
                    updateSubtopic = {},
                    deleteSubtopic = {},
                    navigateBack = {},
                    navigateToSubtopic = {},
                    navigateBackToSubtopics = {}
                )
            }
        }
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbarRow").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertExists()
    }

    @Test
    fun testSubtopicScreen_ExpandedWidthCompactHeight_Success() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.EXPANDED_WIDTH_COMPACT_HEIGHT
            ) {
                SubtopicScreen(
                    uiState = successUiState,
                    updateSubtopic = {},
                    deleteSubtopic = {},
                    navigateBack = {},
                    navigateToSubtopic = {},
                    navigateBackToSubtopics = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Subtopic Title").assertExists()
        composeTestRule.onNodeWithText("Subtopic Description").assertExists()
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbarRow")
    }

    @Test
    fun testSubtopicScreen_ExpandedWidthCompactHeight_Loading() {
        composeTestRule.setContent {
            DeviceConfigurationOverride(
                deviceSize = DeviceSize.EXPANDED_WIDTH_COMPACT_HEIGHT
            ) {
                SubtopicScreen(
                    uiState = loadingUiState,
                    updateSubtopic = {},
                    deleteSubtopic = {},
                    navigateBack = {},
                    navigateToSubtopic = {},
                    navigateBackToSubtopics = {}
                )
            }
        }
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbarRow").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertExists()
    }
}