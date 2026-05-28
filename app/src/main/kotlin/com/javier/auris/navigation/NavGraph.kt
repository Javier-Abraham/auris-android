package com.javier.auris.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.javier.auris.ui.screens.HomeScreen
import com.javier.auris.ui.screens.OnboardingScreen
import com.javier.auris.ui.screens.PlayerScreen
import com.javier.auris.ui.screens.SplashScreen
import com.javier.auris.viewmodel.PlayerViewModel

private object Routes {
    const val SPLASH      = "splash"
    const val ONBOARDING  = "onboarding"
    const val HOME        = "home"
    const val PLAYER      = "player"
}

@Composable
fun AurisNavGraph() {
    val navController = rememberNavController()
    // Single ViewModel instance shared by HomeScreen and PlayerScreen
    val playerViewModel: PlayerViewModel = viewModel()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.HOME) {
            HomeScreen(
                viewModel          = playerViewModel,
                onNavigateToPlayer = { navController.navigate(Routes.PLAYER) },
            )
        }
        composable(Routes.PLAYER) {
            PlayerScreen(
                viewModel = playerViewModel,
                onBack    = { navController.popBackStack() },
            )
        }
    }
}
