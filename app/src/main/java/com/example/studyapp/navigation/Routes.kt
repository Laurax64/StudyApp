package com.example.studyapp.navigation

import kotlinx.serialization.Serializable


@Serializable
data class SubtopicsRoute(val topicId: Int)

@Serializable
data class SubtopicRoute(val subtopicId: Int)
