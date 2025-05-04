package com.example.studyapp.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * An enum class to define different types of email folders or categories.
 */
enum class NavigationItemType {
    STUDY, SAVED, DATES, AI_ASSISTANT,
}

internal data class NavigationItemContent(
    val navigationItemType: NavigationItemType,
    val icon: ImageVector,
    val text: String
)


@Composable
private fun StudyAppNavigationBar(
    currentTab: NavigationItemType,
    onTabPressed: ((NavigationItemType) -> Unit),
    navigationItemContentList: List<NavigationItemContent>,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        for (navItem in navigationItemContentList) {
            NavigationBarItem(
                selected = currentTab == navItem.navigationItemType,
                onClick = { onTabPressed(navItem.navigationItemType) },
                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.text
                    )
                }
            )
        }
    }
}
