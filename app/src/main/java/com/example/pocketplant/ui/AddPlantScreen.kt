package com.example.pocketplant.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.pocketplant.model.Plant
import com.example.pocketplant.ui.theme.PocketPlantTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun AddPlantScreen(
    onAddPlant: (Plant) -> Unit = {} // ✅ Default empty lambda for Preview
) {
    // State for TextFields
    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Plant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = type,
            onValueChange = { type = it },
            label = { Text("Plant Type") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val plant = Plant(
                    id = (0..1000).random(),
                    name = name,
                    type = type,
                    lastWatered = LocalDate.now(),
                    wateringStreak = 0
                )
                onAddPlant(plant)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Plant")
        }
    }
}

/**
 * ✅ Preview Composable
 * Renders the AddPlantScreen without requiring arguments.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewAddPlantScreen() {
    PocketPlantTheme {
        AddPlantScreen()
    }
}
