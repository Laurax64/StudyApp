package com.example.studyapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object TopicsRoute

@Serializable
data class SubtopicsRoute(val topicId: Int)

@Serializable
data class SubtopicRoute(val subtopicId: Int)

@Serializable
data object AIAssistantRoute

@Serializable
data object DatesRoute