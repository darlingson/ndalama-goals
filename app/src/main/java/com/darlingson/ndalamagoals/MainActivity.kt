package com.darlingson.ndalamagoals

import android.content.Intent
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG

import android.os.Build
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
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
import com.darlingson.ndalamagoals.data.repositories.SettingsRepository
import com.darlingson.ndalamagoals.presentation.screens.AddContributionScreen
import com.darlingson.ndalamagoals.presentation.screens.CreateGoalScreen
import com.darlingson.ndalamagoals.presentation.screens.EditGoalScreen
import com.darlingson.ndalamagoals.presentation.screens.GoalDetailScreen
import com.darlingson.ndalamagoals.presentation.screens.GoalsListScreen
import com.darlingson.ndalamagoals.presentation.screens.MyGoalsScreen
import com.darlingson.ndalamagoals.presentation.screens.ProfileScreen
import com.darlingson.ndalamagoals.ui.theme.NdalamaGoalsTheme

class MainActivity : FragmentActivity() {
    private var onAuthSuccess: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val database by lazy { NdalamaDatabase.getDatabase(this) }
        val contributionRepository by lazy { ContributionRepository(database.contributionDao()) }
        val goalRepository by lazy { GoalRepository(database.goalDao()) }
        val factory = viewModelFactory {
            initializer {
                val context = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]!!
                    .applicationContext

                val settingsRepository = SettingsRepository(context)
                appViewModel(contributionRepository, goalRepository, settingsRepository)
            }
        }

        val biometricManager = BiometricManager.from(this)
        val canAuthenticate = biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NdalamaGoalsTheme {
                val navController = rememberNavController()
                val mainViewModel: appViewModel = viewModel(factory = factory)
                val animDuration = 400

                val startDestination = if (canAuthenticate == BiometricManager.BIOMETRIC_SUCCESS) {
                    "auth"
                } else {
                    "goals_list"
                }

                NavHost(
                    navController = navController,
                    startDestination = startDestination,
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
                        AuthRequestScreen(
                            onAuthenticate = {
                                onAuthSuccess = {
                                    navController.navigate("goals_list") {
                                        popUpTo("auth") { inclusive = true }
                                    }
                                }
                                authenticate()
                            }
                        )
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
                    composable("profile") { ProfileScreen(navController, mainViewModel) }
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

                onAuthSuccess?.invoke()
            }
        }

        val executor = ContextCompat.getMainExecutor(this)
        return BiometricPrompt(this, executor, callback)
    }

    private fun createPromptInfo(): BiometricPrompt.PromptInfo =
        BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Confirm your identity to continue")
            .setAllowedAuthenticators(
                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
            )
            .setConfirmationRequired(false)
            .build()
}


@Composable
fun AuthRequestScreen(
    onAuthenticate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // Top branding
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 96.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Ndalama",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Goals",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
            )
        }

        // Center auth card
        Surface(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 4.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier.size(72.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Fingerprint,
                        contentDescription = "Biometric authentication",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    )
                }

                Text(
                    text = "Secure your finances",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Authenticate to continue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                FilledTonalButton(
                    onClick = onAuthenticate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "Authenticate",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        // Bottom hint
        Text(
            text = "Your biometric data never leaves this device",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        )
    }
}

