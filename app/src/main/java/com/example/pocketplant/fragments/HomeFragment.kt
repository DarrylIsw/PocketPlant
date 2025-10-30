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
import androidx.core.os.bundleOf
import com.example.pocketplant.R
import com.example.pocketplant.ui.HomeScreen
import com.example.pocketplant.ui.theme.PocketPlantTheme
import com.example.pocketplant.viewmodel.PlantViewModel

class HomeFragment : Fragment() {

    private val viewModel: PlantViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PocketPlantTheme {
                    HomeScreen(
                        viewModel = viewModel,
                        onPlantClick = { plantId ->
                            findNavController().navigate(
                                R.id.plantDetailFragment,
                                bundleOf("plantId" to plantId)
                            )
                        },
                        onAddClick = {
                            findNavController().navigate(R.id.addPlantFragment)
                        }
                    )
                }
            }
        }
    }
}
