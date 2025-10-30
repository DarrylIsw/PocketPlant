package com.example.pocketplant

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.pocketplant.ui.*
import com.example.pocketplant.viewmodel.PlantViewModel
import com.example.pocketplant.ui.theme.PocketPlantTheme

@RequiresApi(Build.VERSION_CODES.O)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PocketPlantTheme {
                val viewModel: PlantViewModel = viewModel()
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        HomeScreen(
                            viewModel = viewModel,
                            onAddClick = { navController.navigate("add") },
                            onPlantClick = { id -> navController.navigate("detail/$id") }
                        )
                    }

                    composable("add") {
                        AddPlantScreen(
                            navController = navController,
                            viewModel = viewModel,
                            onAddPlant = { plant ->
                                viewModel.addPlant(plant)
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("detail/{plantId}") { backStackEntry ->
                        val plantId = backStackEntry.arguments
                            ?.getString("plantId")
                            ?.toIntOrNull() ?: return@composable

                        PlantDetailScreen(
                            navController = navController,
                            plantId = plantId,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}
