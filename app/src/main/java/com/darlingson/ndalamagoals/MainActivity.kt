package com.darlingson.ndalamagoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                    composable("goals_list") { GoalsListScreen(navController, mainViewModel) }
                    composable("goal_detail/{goalId}") { backStackEntry ->
                        val goalId = backStackEntry.arguments?.getString("goalId")?.toIntOrNull()
                        GoalDetailScreen(navController, mainViewModel, goalId)
                    }
                    composable("create_goal") { CreateGoalScreen(navController, mainViewModel) }
                    composable("edit_goal/{goalId}") { backStackEntry ->
                        val goalId = backStackEntry.arguments?.getString("goalId")?.toIntOrNull()
                        EditGoalScreen(navController, mainViewModel, goalId)
                    }
                    composable("add_contribution/{goalId}") { backStackEntry ->
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
                    composable("my_goals") { MyGoalsScreen(navController, mainViewModel) }
                }
            }
        }
    }
}

