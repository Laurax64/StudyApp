package com.example.studyapp.ui.utils

/**
 * Different type of navigation supported by app depending on size and state.
 */
enum class StudyAppNavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL, PERMANENT_NAVIGATION_DRAWER
}

/**
 * Content shown depending on size and state of device.
 */
enum class StudyAppContentType {
    LIST_ONLY, LIST_AND_DETAIL
}