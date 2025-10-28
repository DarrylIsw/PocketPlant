package com.example.pocketplant.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.pocketplant.R
import com.example.pocketplant.model.Plant
import com.example.pocketplant.ui.HomeScreen
import com.example.pocketplant.ui.theme.PocketPlantTheme
import java.time.LocalDate

class HomeFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Return ComposeView with Compose content
        return ComposeView(requireContext()).apply {
            setContent {
                PocketPlantTheme {
                    HomeScreen(
                        plants = samplePlants(),
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun samplePlants() = listOf(
        Plant(1, "Aloe Vera", "Succulent", LocalDate.now(), 3),
        Plant(2, "Fiddle Leaf Fig", "Tree", LocalDate.now().minusDays(1), 5)
    )
}
