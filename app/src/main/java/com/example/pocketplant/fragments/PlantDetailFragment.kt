package com.example.pocketplant.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.compose.rememberNavController
import com.example.pocketplant.ui.PlantDetailScreen
import com.example.pocketplant.ui.theme.PocketPlantTheme
import com.example.pocketplant.viewmodel.PlantViewModel

class PlantDetailFragment : Fragment() {

    private val plantViewModel: PlantViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val plantId = arguments?.getInt("plantId") ?: -1

        return ComposeView(requireContext()).apply {
            setContent {
                PocketPlantTheme {
                    // Each fragment can safely create its own NavController for Compose screens
                    val navController = rememberNavController()

                    PlantDetailScreen(
                        navController = navController,
                        plantId = plantId,
                        viewModel = plantViewModel
                    )
                }
            }
        }
    }
}
