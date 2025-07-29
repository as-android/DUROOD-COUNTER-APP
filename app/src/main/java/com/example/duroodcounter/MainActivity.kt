package com.example.duroodcounter

import CountingScreen
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.duroodcounter.data.DataStoreManager
import com.example.duroodcounter.notification.scheduleDuroodReminders
import com.example.duroodcounter.screens.WelcomeDialog
import com.example.duroodcounter.screens.addscreen.AddScreen
import com.example.duroodcounter.screens.addscreen.DuroodListScreen
import com.example.duroodcounter.screens.addscreen.PreviewDuroodScreen
import com.example.duroodcounter.screens.detailscreen.CounterScreen
import com.example.duroodcounter.screens.home.AboutScreen
import com.example.duroodcounter.screens.home.HomeScreen
import com.example.duroodcounter.screens.splash.SplashScreen
import com.example.duroodcounter.ui.theme.DuroodCounterTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Ask permission if Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
        scheduleDuroodReminders(this)
        setContent {

            DuroodCounterTheme {
                val navController = rememberNavController()
                val context = LocalContext.current
                val dataStoreManager = remember { DataStoreManager(context) }
                var showWelcome by remember { mutableStateOf(false) }

                LaunchedEffect(Unit) {
                    if (dataStoreManager.isFirstLaunch()) {
                        showWelcome = true
                        dataStoreManager.setFirstLaunchDone()
                    }
                }

                if (showWelcome) {
                    WelcomeDialog(onDismiss = { showWelcome = false })
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = "splash_screen",
                        enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
                        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
                        popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                        popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) },
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("splash_screen") {
                            SplashScreen(navController = navController)
                        }
                        composable("home") {
                                HomeScreen(
                                    navController = navController
                                )
                        }
                        composable("counter/{counterId}") { backStackEntry ->
                            val counterId = backStackEntry.arguments?.getString("counterId")?.toIntOrNull()
                            if (counterId != null) {
                                CounterScreen(navController = navController,counterId = counterId, onNavigateBack = { navController.popBackStack() })

                            }
                        }
                        composable("addscreen") {
                            AddScreen(
                                navController = navController
                            )
                        }
                        composable("counting_screen/{counterId}/{title}/{remaining}") { backStackEntry ->
                            val counterId = backStackEntry.arguments?.getString("counterId")?.toIntOrNull() ?: 0
                            val title = backStackEntry.arguments?.getString("title") ?: ""
                            val remaining = backStackEntry.arguments?.getString("remaining")?.toIntOrNull() ?: 0
                            CountingScreen(
                                counterId = counterId,
                                counterTitle = title,
                                counterTarget = remaining,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable("durood_list_screen") {
                            DuroodListScreen(navController)
                        }

                        composable("preview_screen/{titleResId}") { backStackEntry ->
                            val resId = backStackEntry.arguments?.getString("titleResId")?.toIntOrNull() ?: 0
                            PreviewDuroodScreen(
                                titleResId = resId,
                                navController = navController
                            )
                        }
                        composable("about_screen") {
                            AboutScreen(
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }
}