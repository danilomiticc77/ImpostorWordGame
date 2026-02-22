package com.example.impostorgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.impostorgame.ui.navigation.AppNavigation
import com.example.impostorgame.ui.theme.ImpostorGameTheme
import com.example.impostorgame.viewmodel.GameViewModel

/**
 * Single-activity entry point for the Impostor Game.
 * Uses Compose navigation with a shared GameViewModel.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpostorGameTheme {
                val navController = rememberNavController()
                val gameViewModel: GameViewModel = viewModel()

                androidx.compose.material3.Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = com.example.impostorgame.ui.theme.DarkBackground
                ) {
                    AppNavigation(
                        navController = navController,
                        viewModel = gameViewModel
                    )
                }
            }
        }
    }
}