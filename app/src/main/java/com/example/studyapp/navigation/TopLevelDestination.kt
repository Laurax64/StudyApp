package com.example.studyapp.navigation

import com.example.studyapp.R
import kotlin.reflect.KClass

enum class TopLevelDestination(
    val iconRes: Int,
    val contentDescriptionRes: Int,
    val labelRes: Int,
    val route: KClass<*>,
) {
    STUDY(
        iconRes = R.drawable.baseline_school_24,
        contentDescriptionRes = R.string.go_to_study,
        labelRes = R.string.study,
        route = TopicsRoute::class
    ),
    DATES(
        iconRes = R.drawable.baseline_event_24,
        contentDescriptionRes = R.string.go_to_dates,
        labelRes = R.string.dates,
        route = DatesRoute::class
    ),
    AI_ASSISTANT(
        iconRes = R.drawable.baseline_assistant_24,
        contentDescriptionRes = R.string.go_to_ai_assistant,
        labelRes = R.string.ai_assistant,
        route = AIAssistantRoute::class
    ),
}
