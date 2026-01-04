package com.darlingson.ndalamagoals

import android.content.Intent
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.darlingson.ndalamagoals.presentation.screens.EditGoalScreen
import com.darlingson.ndalamagoals.presentation.screens.GoalDetailScreen
import com.darlingson.ndalamagoals.presentation.screens.GoalsListScreen
import com.darlingson.ndalamagoals.presentation.screens.MyGoalsScreen
import com.darlingson.ndalamagoals.presentation.screens.ProfileScreen
import com.darlingson.ndalamagoals.ui.theme.NdalamaGoalsTheme

class MainActivity : FragmentActivity() {
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
                val animDuration = 400

                NavHost(
                    navController = navController,
                    startDestination = "auth",
                    enterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(animDuration)
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(animDuration)
                        )
                    },
                    popEnterTransition = {
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(animDuration)
                        )
                    },
                    popExitTransition = {
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(animDuration)
                        )
                    }
                ) {
                    composable("auth") {
                        AuthScreen(onAuthenticate = { authenticate() })
                    }
                    composable(
                        "goals_list",
                    ) {
                        GoalsListScreen(navController, mainViewModel)
                    }
                    composable(
                        "goal_detail/{goalId}"
                    ) { backStackEntry ->
                        val goalId = backStackEntry.arguments?.getString("goalId")?.toIntOrNull()
                        GoalDetailScreen(navController, mainViewModel, goalId)
                    }
                    composable(
                        "create_goal"
                    ) { CreateGoalScreen(navController, mainViewModel) }
                    composable(
                        "edit_goal/{goalId}"
                    ) { backStackEntry ->
                        val goalId = backStackEntry.arguments?.getString("goalId")?.toIntOrNull()
                        EditGoalScreen(navController, mainViewModel, goalId)
                    }
                    composable(
                        "add_contribution/{goalId}"
                    ) { backStackEntry ->
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
                    composable(
                        "my_goals",
                    ) { MyGoalsScreen(navController, mainViewModel) }
                    composable("profile") { ProfileScreen(navController) }
                }
            }
        }
    }


    private fun authenticate() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                val prompt = createBiometricPrompt()
                prompt.authenticate(createPromptInfo())
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(this, "Biometric hardware is missing", Toast.LENGTH_SHORT).show()
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(this, "Biometric is currently unavailable", Toast.LENGTH_SHORT)
                    .show()
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    this,
                    "Enrolling in biometrics that your app accepts",
                    Toast.LENGTH_SHORT,
                ).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    val enrollIntent =
                        Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                            putExtra(
                                Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                            )
                        }
                    startActivityForResult(enrollIntent, 1333)
                }
            }
        }
    }

    private fun createBiometricPrompt(): BiometricPrompt {
        val callback = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@MainActivity, "AuthenticationError $errorCode $errString", Toast.LENGTH_SHORT).show()
                if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                    Toast.makeText(this@MainActivity, "Negative button pressed", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@MainActivity, "Unknown authentication error", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(
                    this@MainActivity,
                    "Authenticated with biometrics successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val executor = ContextCompat.getMainExecutor(this)
        return BiometricPrompt(this, executor, callback)
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate with biometrics")
            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .setConfirmationRequired(false)
            .setNegativeButtonText("Login with password")
            .build()
}


@Composable
fun AuthScreen(onAuthenticate: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Button(
            modifier = Modifier.align(Alignment.Center),
            onClick = onAuthenticate,
        ) {
            Text(text = "Authenticate")
        }
    }
}
