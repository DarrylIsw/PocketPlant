package com.example.pocketplant.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.example.pocketplant.model.Plant
import com.example.pocketplant.ui.PlantDetailScreen
import com.example.pocketplant.ui.theme.PocketPlantTheme
import java.time.LocalDate

class PlantDetailFragment : Fragment() {

    private var plant: Plant? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val plantId = arguments?.getInt("plantId") ?: 0
        // In a real app, fetch plant from ViewModel or database
        plant = samplePlants().find { it.id == plantId }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                PocketPlantTheme {
                    plant?.let { p ->
                        PlantDetailScreen(
                            plant = p,
                            onWatered = { oldPlant ->
                                // Create an updated copy (data class is immutable)
                                val updatedPlant = oldPlant.copy(
                                    wateringStreak = oldPlant.wateringStreak + 1,
                                    lastWatered = LocalDate.now()
                                )
                                // In real app: update ViewModel or DB here
                            }
                        )
                    }
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
