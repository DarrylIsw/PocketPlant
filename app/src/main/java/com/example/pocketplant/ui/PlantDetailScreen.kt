package com.example.pocketplant.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
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
fun PlantDetailScreen(
    plant: Plant = Plant( // ✅ Default for preview
        id = 0,
        name = "Aloe Vera",
        type = "Succulent",
        lastWatered = LocalDate.now().minusDays(2),
        wateringStreak = 3
    ),
    onWatered: (Plant) -> Unit = {} // ✅ Default empty lambda
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text(plant.name) }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Type: ${plant.type}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Last Watered: ${plant.lastWatered}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Watering Streak: ${plant.wateringStreak} 💧",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onWatered(plant) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Water Plant")
            }
        }
    }
}

/**
 * ✅ Preview Composable for PlantDetailScreen
 */
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewPlantDetailScreen() {
    PocketPlantTheme {
        PlantDetailScreen()
    }
}
