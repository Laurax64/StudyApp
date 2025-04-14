package com.example.studyapp.navigation

import kotlinx.serialization.Serializable

@Serializable
object TopicsRoute

@Serializable
data class SubtopicsRoute(val topicId: Int)

@Serializable
object SubtopicRoute
