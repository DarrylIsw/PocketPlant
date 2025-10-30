package com.example.pocketplant.ui

import android.app.TimePickerDialog
import android.net.Uri
import android.os.Build
import android.widget.TimePicker
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.pocketplant.model.Plant
import com.example.pocketplant.ui.theme.PocketPlantTheme
import com.example.pocketplant.viewmodel.PlantViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AddPlantScreen(
    navController: NavController,
    viewModel: PlantViewModel,
    onAddPlant: (Plant) -> Unit
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) imageUri = uri
    }

    var name by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("") }
    var sunlight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val wateringHours = remember { mutableStateListOf<String>() }
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Function to open time picker
    fun openTimePicker() {
        val now = LocalTime.now()
        TimePickerDialog(
            context,
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                val selectedTime = LocalTime.of(hourOfDay, minute)
                val formatted = selectedTime.format(timeFormatter)
                if (!wateringHours.contains(formatted)) {
                    wateringHours.add(formatted)
                }
            },
            now.hour,
            now.minute,
            true
        ).show()
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Add New Plant") }) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (name.isBlank() || type.isBlank()) return@FloatingActionButton

                    val newPlant = Plant(
                        id = (viewModel.plants.value?.size ?: 0) + 1,
                        name = name,
                        type = type,
                        lastWatered = LocalDate.now(),
                        wateringStreak = 0,
                        imageUri = imageUri,
                        wateringHours = wateringHours.toList(),
                        sunlight = sunlight,
                        notes = notes
                    )

                    viewModel.addPlant(newPlant)
                    navController.popBackStack()
                }
            ) {
                Icon(Icons.Default.Check, contentDescription = "Save Plant")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(scrollState)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- Image Upload ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(imageUri)
                                .build()
                        ),
                        contentDescription = "Plant Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Tap to upload image", color = Color.DarkGray)
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Plant Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Plant Type Dropdown ---
            val plantTypes = listOf(
                "Succulent", "Cactus", "Fern", "Ornamental Grass", "Flowering Plant",
                "Herb", "Shrub", "Tree", "Indoor Plant", "Vine/Climber", "Aquatic Plant", "Other"
            )
            var expanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Plant Type") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    plantTypes.forEach { plantType ->
                        DropdownMenuItem(
                            text = { Text(plantType) },
                            onClick = {
                                type = plantType
                                expanded = false
                            }
                        )
                    }
                }
            }

            // --- Watering Hours ---
            Column {
                Text("Watering Hours", style = MaterialTheme.typography.labelLarge)
                Button(onClick = { openTimePicker() }) { Text("Add Watering Hour") }

                if (wateringHours.isNotEmpty()) {
                    wateringHours.toList().forEach { hour ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(hour)
                            Text(
                                "Remove",
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { wateringHours.remove(hour) }
                            )
                        }
                    }
                }
            }

            // --- Sunlight Dropdown ---
            val sunlightOptions =
                listOf("Full Sun", "Partial Sun / Partial Shade", "Shade / Low Light")
            var sunlightExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                expanded = sunlightExpanded,
                onExpandedChange = { sunlightExpanded = !sunlightExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = sunlight,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Sunlight Requirement") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(sunlightExpanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = sunlightExpanded,
                    onDismissRequest = { sunlightExpanded = false }
                ) {
                    sunlightOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                sunlight = option
                                sunlightExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewAddPlantScreen() {
    PocketPlantTheme {
        // For preview only, no real navigation or viewmodel
        // Replace with fake NavController and ViewModel if needed
        Text("Preview Add Plant Screen")
    }
}



/**
 * ✅ Preview Composable
 * Renders the AddPlantScreen without requiring arguments.
 */
//@RequiresApi(Build.VERSION_CODES.O)
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun PreviewAddPlantScreen() {
//    PocketPlantTheme {
//        AddPlantScreen()
//    }
//}
