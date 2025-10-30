package com.example.pocketplant.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.pocketplant.viewmodel.PlantViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.pocketplant.ui.theme.PocketPlantTheme
import java.time.LocalDate

@Composable
fun HomeScreen(
    viewModel: PlantViewModel,
    onAddClick: () -> Unit,
    onPlantClick: (Int) -> Unit
) {
    // Observe LiveData from ViewModel
    val plants by viewModel.plants.observeAsState(emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Text("+")
            }
        }
    ) { padding ->
        if (plants.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No plants yet. Add one!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(plants) { plant ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onPlantClick(plant.id) },
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            plant.imageUri?.let {
                                Image(
                                    painter = rememberAsyncImagePainter(it),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .padding(end = 12.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = plant.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = plant.type,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



/**
 * ✅ Preview for HomeScreen
 * Shows a small list of plants in a themed preview.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeScreen_Simple() {
    val fakePlants = listOf(
        com.example.pocketplant.model.Plant(
            id = 1,
            name = "Aloe Vera",
            type = "Succulent",
            imageUri = null,
            wateringHours = listOf("07:00", "19:00"),
            sunlight = "Full Sun",
            notes = "Water lightly every 3 days.",
            lastWatered = LocalDate.now(),
            wateringStreak = 5
        ),
        com.example.pocketplant.model.Plant(
            id = 2,
            name = "Fern",
            type = "Indoor Plant",
            imageUri = null,
            wateringHours = listOf("08:00"),
            sunlight = "Low Light",
            notes = "Keep soil moist.",
            lastWatered = LocalDate.now(),
            wateringStreak = 2
        )
    )

    PocketPlantTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            fakePlants.forEach { plant ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = plant.name, style = MaterialTheme.typography.titleMedium)
                        Text(text = plant.type, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}



