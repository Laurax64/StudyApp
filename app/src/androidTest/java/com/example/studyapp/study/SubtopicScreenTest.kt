package com.example.studyapp.study

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.example.studyapp.data.Subtopic
import com.example.studyapp.ui.subtopic.SubtopicScreen
import com.example.studyapp.ui.subtopic.SubtopicUiState
import com.example.studyapp.utils.DeviceConfigurationOverride
import com.example.studyapp.utils.DeviceSize
import org.junit.Rule
import org.junit.Test

/**
 * Tests for [com.example.studyapp.ui.subtopic.SubtopicScreen].
 *
 * It contains methods for each relevant screen height and width for the different uiStates.
 * The assertions in each method are grouped by the different views defined in the
 * [TopicsScreen Figma design file](https://www.figma.com/design/PFv6qgJRGjVoNkekrOewZM/StudyApp?node-id=4-1857&p=f&t=JPnkfQGNJnCnpTBx-0).
 *
 */
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
        // Base
        composeTestRule.onNodeWithText("Subtopic Title").assertExists()
        composeTestRule.onNodeWithText("Subtopic Description").assertExists()
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbar")
        // Delete
        composeTestRule.onNodeWithContentDescription("Open delete subtopic dialog").performClick()
        composeTestRule.onNodeWithText("Delete subtopic?").assertExists()
        composeTestRule.onNodeWithText("Cancel").performClick()
        // Edit
        composeTestRule.onNodeWithContentDescription("Open edit subtopic dialog").performClick()
        composeTestRule.onNodeWithText("Edit subtopic").assertExists()
        composeTestRule.onNodeWithContentDescription("Cancel").performClick()
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
        // Loading
        composeTestRule.onNodeWithTag("SubtopicToolbar").assertDoesNotExist()
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
        // Base
        composeTestRule.onNodeWithText("Subtopic Title").assertExists()
        composeTestRule.onNodeWithText("Subtopic Description").assertExists()
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbarRow")
        // Delete
        composeTestRule.onNodeWithContentDescription("Open delete subtopic dialog").performClick()
        composeTestRule.onNodeWithText("Delete subtopic?").assertExists()
        composeTestRule.onNodeWithText("Cancel").performClick()
        // Edit
        composeTestRule.onNodeWithContentDescription("Open edit subtopic dialog").performClick()
        composeTestRule.onNodeWithText("Edit subtopic").assertExists()
        composeTestRule.onNodeWithContentDescription("Cancel").performClick()
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
        // Loading
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
        // Base
        composeTestRule.onNodeWithText("Subtopic Title").assertExists()
        composeTestRule.onNodeWithText("Subtopic Description").assertExists()
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbarRow")
        // Delete
        composeTestRule.onNodeWithContentDescription("Open delete subtopic dialog").performClick()
        composeTestRule.onNodeWithText("Delete subtopic?").assertExists()
        composeTestRule.onNodeWithText("Cancel").performClick()
        // Edit
        composeTestRule.onNodeWithContentDescription("Open edit subtopic dialog").performClick()
        composeTestRule.onNodeWithText("Edit subtopic").assertExists()
        composeTestRule.onNodeWithText("Cancel").performClick()
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
        // Loading
        composeTestRule.onNodeWithTag("SubtopicFloatingToolbarRow").assertDoesNotExist()
        composeTestRule.onNodeWithTag("LoadingIndicatorBox").assertExists()
    }
}