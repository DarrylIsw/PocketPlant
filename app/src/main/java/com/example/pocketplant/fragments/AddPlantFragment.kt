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
import androidx.navigation.fragment.findNavController
import com.example.pocketplant.R
import com.example.pocketplant.ui.AddPlantScreen
import com.example.pocketplant.ui.theme.PocketPlantTheme
import com.example.pocketplant.viewmodel.PlantViewModel

class AddPlantFragment : Fragment() {

    private val plantViewModel: PlantViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PocketPlantTheme {
                    AddPlantScreen(
                        navController = findNavController(),
                        viewModel = plantViewModel,
                        onAddPlant = { plant ->
                            plantViewModel.addPlant(plant)
                            findNavController().navigate(
                                R.id.action_addPlantFragment_to_plantDetailFragment,
                                Bundle().apply { putInt("plantId", plant.id) }
                            )
                        }
                    )

                }
            }
        }
    }
}
