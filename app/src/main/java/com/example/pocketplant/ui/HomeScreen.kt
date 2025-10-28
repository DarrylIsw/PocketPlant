package com.example.pocketplant.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pocketplant.model.Plant
import com.example.pocketplant.ui.theme.PocketPlantTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    plants: List<Plant> = emptyList(), // ✅ default empty list for preview
    onPlantClick: (Int) -> Unit = {},  // ✅ default empty lambdas
    onAddClick: () -> Unit = {}
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pocket Plant") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Plant")
            }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(plants) { plant ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { onPlantClick(plant.id) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = plant.name,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = "Last Watered: ${plant.lastWatered}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Text(
                            text = "${plant.wateringStreak} 💧",
                            style = MaterialTheme.typography.bodyLarge
                        )
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
fun PreviewHomeScreen() {
    val samplePlants = listOf(
        Plant(1, "Aloe Vera", "Succulent", LocalDate.now(), 3),
        Plant(2, "Fiddle Leaf Fig", "Tree", LocalDate.now().minusDays(2), 5),
        Plant(3, "Snake Plant", "Indoor", LocalDate.now().minusDays(1), 2)
    )

    PocketPlantTheme {
        HomeScreen(plants = samplePlants)
    }
}
