package com.example.studyapp.navigation

import kotlinx.serialization.Serializable


@Serializable
data class TopicsRoute(
    // The ID of the topic which will be initially selected at this destination
    val initialTopicId: Int? = null,
)

@Serializable
internal object TopicPlaceholderRoute

@Serializable
data class TopicRoute(val id: Int)

@Serializable
data class SubtopicRoute(val subtopicId: Int)
