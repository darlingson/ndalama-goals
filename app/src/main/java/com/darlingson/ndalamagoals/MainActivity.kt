package com.darlingson.ndalamagoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.darlingson.ndalamagoals.data.NdalamaDatabase
import com.darlingson.ndalamagoals.data.appViewModel
import com.darlingson.ndalamagoals.data.repositories.ContributionRepository
import com.darlingson.ndalamagoals.data.repositories.GoalRepository
import com.darlingson.ndalamagoals.presentation.screens.AddContributionScreen
import com.darlingson.ndalamagoals.presentation.screens.CreateGoalScreen
import com.darlingson.ndalamagoals.presentation.screens.GoalDetailScreen
import com.darlingson.ndalamagoals.presentation.screens.GoalsListScreen
import com.darlingson.ndalamagoals.ui.theme.NdalamaGoalsTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.darlingson.ndalamagoals.presentation.screens.EditGoalScreen
import com.darlingson.ndalamagoals.presentation.screens.MyGoalsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val database by lazy { NdalamaDatabase.getDatabase(this) }
        val contributionRepository by lazy { ContributionRepository(database.contributionDao()) }
        val goalRepository by lazy { GoalRepository(database.goalDao()) }
        val factory = viewModelFactory {
            initializer {
                appViewModel(contributionRepository, goalRepository)
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NdalamaGoalsTheme {
                val navController = rememberNavController()
                val mainViewModel: appViewModel = viewModel(factory = factory)

                NavHost(navController = navController, startDestination = "goals_list") {
                    composable("goals_list",enterTransition = {
                        fadeIn(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    },
                        exitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + slideOutOfContainer(
                                animationSpec = tween(300, easing = EaseOut),
                                towards = AnimatedContentTransitionScope.SlideDirection.End
                            )
                        }) { GoalsListScreen(navController, mainViewModel) }
                    composable("goal_detail/{goalId}",
                        enterTransition = {
                            fadeIn(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + slideIntoContainer(
                                animationSpec = tween(300, easing = EaseIn),
                                towards = AnimatedContentTransitionScope.SlideDirection.Start
                            )
                        },
                        exitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + slideOutOfContainer(
                                animationSpec = tween(300, easing = EaseOut),
                                towards = AnimatedContentTransitionScope.SlideDirection.End
                            )
                        }
                        ) { backStackEntry ->
                        val goalId = backStackEntry.arguments?.getString("goalId")?.toIntOrNull()
                        GoalDetailScreen(navController, mainViewModel, goalId)
                    }
                    composable("create_goal",
                        enterTransition = { slideInHorizontally(initialOffsetX = { 1000 }, animationSpec = tween(700)) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -1000 }, animationSpec = tween(700)) },
                        popEnterTransition = { slideInHorizontally(initialOffsetX = { -1000 }, animationSpec = tween(700)) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { 1000 }, animationSpec = tween(700)) }
                        ) { CreateGoalScreen(navController, mainViewModel) }
                    composable("edit_goal/{goalId}", enterTransition = {
                        fadeIn(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    },
                        exitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + slideOutOfContainer(
                                animationSpec = tween(300, easing = EaseOut),
                                towards = AnimatedContentTransitionScope.SlideDirection.End
                            )
                        }) { backStackEntry ->
                        val goalId = backStackEntry.arguments?.getString("goalId")?.toIntOrNull()
                        EditGoalScreen(navController, mainViewModel, goalId)
                    }
                    composable("add_contribution/{goalId}", enterTransition = {
                        fadeIn(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    },
                        exitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + slideOutOfContainer(
                                animationSpec = tween(300, easing = EaseOut),
                                towards = AnimatedContentTransitionScope.SlideDirection.End
                            )
                        }) { backStackEntry ->
                        val goalId = backStackEntry.arguments?.getString("goalId")?.toIntOrNull()
                        AddContributionScreen(
                            onBack = {
                                navController.popBackStack()
                            },
                            onSave = {
                                navController.popBackStack()
                            },
                            mainViewModel,
                            goalId
                        )
                    }
                    composable("my_goals", enterTransition = {
                        fadeIn(
                            animationSpec = tween(
                                300, easing = LinearEasing
                            )
                        ) + slideIntoContainer(
                            animationSpec = tween(300, easing = EaseIn),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    },
                        exitTransition = {
                            fadeOut(
                                animationSpec = tween(
                                    300, easing = LinearEasing
                                )
                            ) + slideOutOfContainer(
                                animationSpec = tween(300, easing = EaseOut),
                                towards = AnimatedContentTransitionScope.SlideDirection.End
                            )
                        }) { MyGoalsScreen(navController, mainViewModel) }
                }
            }
        }
    }
}

