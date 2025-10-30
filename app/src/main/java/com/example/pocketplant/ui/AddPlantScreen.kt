package com.example.pocketplant.ui

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun AddPlantScreen(
    onAddPlant: (Plant) -> Unit = {}
) {
    // Scrollable container to prevent dropdown from being cut off
    val scrollState = rememberScrollState()
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // Launch image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) imageUri = uri
    }

    var name by remember { mutableStateOf("") }
    // Additional fields
    var wateringInterval by remember { mutableStateOf("") }
    var sunlight by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
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

        Spacer(modifier = Modifier.height(16.dp))

        // --- Plant Name ---
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Plant Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- Plant Type Dropdown ---
        val plantTypes = listOf(
            "Succulent", "Cactus", "Fern", "Ornamental Grass", "Flowering Plant",
            "Herb", "Shrub", "Tree", "Indoor Plant", "Vine/Climber", "Aquatic Plant", "Other"
        )
        var type by remember { mutableStateOf("") }
        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = type,
                onValueChange = { },
                readOnly = true,
                label = { Text("Plant Type") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier
                    .menuAnchor() // important to overlay other fields
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
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

        Spacer(modifier = Modifier.height(8.dp))

        val context = LocalContext.current
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

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Watering Hours", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(4.dp))

            // Button to launch TimePicker
            Button(onClick = { openTimePicker() }) {
                Text("Add Watering Hour")
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Show added hours safely
            if (wateringHours.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Use toList() to avoid mutation during iteration issues
                    wateringHours.toList().forEach { hour ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
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
        }

        Spacer(modifier = Modifier.height(8.dp))

        val sunlightOptions = listOf("Full Sun", "Partial Sun / Partial Shade", "Shade / Low Light")
        var sunlight by remember { mutableStateOf("") }
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
                onDismissRequest = { sunlightExpanded = false },
                modifier = Modifier.fillMaxWidth()
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

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Save Button ---
        Button(
            onClick = {
                if (name.isBlank() || type.isBlank()) return@Button

                val plant = Plant(
                    id = (0..1000).random(),
                    name = name,
                    type = type,
                    lastWatered = LocalDate.now(),
                    wateringStreak = 0,
                    imageUri = imageUri,
                    wateringHours = wateringHours.toList(),
                    sunlight = sunlight,
                    notes = notes
                )



                onAddPlant(plant)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
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
