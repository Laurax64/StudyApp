package com.example.studyapp.navigation

/**
@Composable
fun StudyAppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
// TODO: delete or adapt this to the new layout
/*
    NavHost(
        navController = navController, startDestination = TopicsRoute, modifier = modifier
    ) {
        composable<TopicsRoute> {
            TopicsScreen(
                topicsViewModel = hiltViewModel<TopicsViewModel>(),
                navigateToTopic = { navController.navigate(route = SubtopicsRoute(topicId = it)) }
            )
        }
        composable<SubtopicsRoute> {
            SubtopicsScreen(
                subtopicsViewModel = hiltViewModel<SubtopicsViewModel>(),
                navigateToSubtopic = { subtopicId ->
                    navController.navigate(route = SubtopicRoute(subtopicId = subtopicId))
                },
                navigateBack = navController::popBackStack
            )
        }

        composable<SubtopicRoute> { entry ->
            val subtopicId = entry.toRoute<SubtopicRoute>().subtopicId
            SubtopicScreen(
                subtopicViewModel = hiltViewModel<SubtopicViewModel, SubtopicViewModel.Factory>(
                    key = subtopicId.toString()
                ) { factory ->
                    factory.create(subtopicId)
                },
                navigateBack = navController::popBackStack
            )
        }


}
 */