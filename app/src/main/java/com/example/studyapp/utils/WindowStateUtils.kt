package com.example.studyapp.utils

/**
 * Different type of navigation supported by app depending on size and state.
 */
enum class StudyAppNavigationType {
    BOTTOM_NAVIGATION, NAVIGATION_RAIL
}

/**
 * Content shown depending on size and state of device.
 */
enum class StudyAppContentType {
    LIST_ONLY, LIST_AND_DETAIL
}