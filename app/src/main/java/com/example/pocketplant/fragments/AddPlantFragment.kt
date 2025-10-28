package com.example.pocketplant.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pocketplant.model.Plant
import com.example.pocketplant.ui.AddPlantScreen
import com.example.pocketplant.ui.theme.PocketPlantTheme

class AddPlantFragment : Fragment() {

    // Shared list of plants (use ViewModel in real app)
    private val plantList = mutableStateListOf<Plant>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // IMPORTANT: create ComposeView and explicitly call its member function
        val composeView = ComposeView(requireContext())
        composeView.setContent {
            PocketPlantTheme {
                AddPlantScreen { plant ->
                    plantList.add(plant)
                    findNavController().navigateUp()
                }
            }
        }
        return composeView
    }
}
