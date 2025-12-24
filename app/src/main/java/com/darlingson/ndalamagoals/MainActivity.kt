package com.darlingson.ndalamagoals

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.darlingson.ndalamagoals.presentation.screens.AddContributionScreen
import com.darlingson.ndalamagoals.presentation.screens.CreateGoalScreen
import com.darlingson.ndalamagoals.presentation.screens.GoalDetailScreen
import com.darlingson.ndalamagoals.presentation.screens.GoalsListScreen
import com.darlingson.ndalamagoals.ui.theme.NdalamaGoalsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NdalamaGoalsTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "goals_list") {
                    composable("goals_list") { GoalsListScreen(navController) }
                    composable("goal_detail") { GoalDetailScreen(navController) }
                    composable("create_goal") { CreateGoalScreen(navController) }
                    composable("edit_goal") { CreateGoalScreen(navController) }
                    composable("add_contribution") {
                        AddContributionScreen(
                            onBack = {
                                navController.popBackStack()
                            },
                            onSave = {
                                // Add your save logic here, then navigate back
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}

